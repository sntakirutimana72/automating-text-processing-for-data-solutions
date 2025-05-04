package com.automating_text_processing.service;

import com.automating_text_processing.DTO.*;
import com.automating_text_processing.exception.ExtractionBatchNotFound;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextExtractorService extends TextProcessingCoreService {
  private ScheduledFuture<?> extractionCleaningFactory;
  private final AtomicInteger extractionVersionControl;
  private final ConcurrentHashMap<Integer, List<Future<?>>> extractionJobs;

  public TextExtractorService() {
    super();
    extractionVersionControl = new AtomicInteger(0);
    extractionJobs = new ConcurrentHashMap<>();
  }

  private List<Future<?>> registerExtraction(int version) {
    List<Future<?>> jobs = extractionJobs.put(version, new ArrayList<>());
    return Objects.isNull(jobs) ? extractionJobs.get(version) : jobs;
  }

  private void runExtraction(ExtractionRule rule) {
    // Processed line size
    AtomicLong processed = new AtomicLong(0);
    // Total file size, initially set to 1
    AtomicLong total = new AtomicLong(1);
    // Track partial error
    AtomicBoolean partiallyFailed = new AtomicBoolean();
    // Construct progress factory
    BiConsumer<ExtractionStatus, String> emit = (status, error) -> rule.getProgressEmitter()
      .accept(new ExtractionProgress(
        rule.getVersion(),
        (double) processed.get() / total.get() * 100,
        rule.getSource().toString(),
        status,
        error
      ));
    // Enter extraction logic body
    try(BufferedWriter writer = Files.newBufferedWriter(rule.getDestination().getPath(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
      // Get total file size
      total.addAndGet(Files.size(rule.getSource()));
      // Define read rule to be passed down to the read core functionality
      ReadRule readRule = new ReadRule(rule.getSource().toString(), (line) -> {
        if (Thread.currentThread().isInterrupted())
          throw new RuntimeException();
        // Add current data size to total processed
        processed.addAndGet(line.getBytes(StandardCharsets.UTF_8).length);
        // Broadcast progress
        emit.accept(ExtractionStatus.ACTIVE, null);
        // Now, run search
        Matcher matcher = Pattern.compile(rule.getRegex()).matcher(line);
        // Acquire write lock
        synchronized (rule.getWriteLock()) {
          while (!Thread.currentThread().isInterrupted() && matcher.find()) {
            String group = matcher.group();
            if (Objects.nonNull(group)) {
              try {
                writer.write(group);
                writer.newLine();
              } catch (IOException ignored) {
                partiallyFailed.set(true);
              }
            }
          }
        }
        // If matcher still have data, it means thread was interrupted, so throw an exception
        if (matcher.find())
          throw new RuntimeException();
      });
      // Now, read
      read(readRule);
      // Check if there were partial failures
      emit.accept(partiallyFailed.get() ? ExtractionStatus.PARTIAL : ExtractionStatus.COMPLETE, null);
    }
    catch (RuntimeException ignored) {
      emit.accept(ExtractionStatus.CANCELLED, null);
    }
    catch (Exception e) {
      System.err.println(e.getMessage());
      emit.accept(ExtractionStatus.FAILED, e.getMessage());
    }
  }

  public void extract(ExtractorRule rule) {
    ensureExtractionCleaningFactoryIsLive();
    List<Future<?>> jobs = registerExtraction(rule.getVersion());
    // Initiate batch file write lock to avoid race condition
    Object writeLock = new Object();
    // Initiate tasks service runner
    ExecutorService service = Executors.newFixedThreadPool(4);
    for (PathLike source : rule.getSources()) {
      ExtractionRule extractionRule = new ExtractionRule(
        source.getPath(),
        writeLock,
        rule.getVersion(),
        rule.getRegex(),
        rule.getDestination(),
        rule.getProgressEmitter()
      );
      jobs.add(service.submit(() -> runExtraction(extractionRule)));
    }
    service.shutdown();
  }

  private void ensureExtractionCleaningFactoryIsLive() {
    if (Objects.isNull(extractionCleaningFactory)) {
      ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
      scheduler.scheduleAtFixedRate(() -> extractionJobs.forEachKey(Integer.MAX_VALUE, key -> {
        try {
          cancelExtractionByBatch(key);
        } catch (ExtractionBatchNotFound ignored) {}
      }), 0, 5, TimeUnit.SECONDS);
      scheduler.shutdown();
    }
  }

  public void cancelExtractionByBatch(int version) throws ExtractionBatchNotFound {
    List<Future<?>> batch = extractionJobs.get(version);
    if (Objects.isNull(batch)) throw new ExtractionBatchNotFound(version);
    batch.removeIf(task -> task.isCancelled() || task.isDone());
    // Remove batch itself if empty
    if (batch.isEmpty()) extractionJobs.remove(version);
  }

  public int newExtractionVersion() {
    return extractionVersionControl.incrementAndGet();
  }
}
