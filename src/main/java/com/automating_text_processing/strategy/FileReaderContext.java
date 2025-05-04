package com.automating_text_processing.strategy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileReaderContext {
  private static final long LARGE_FILE_THRESHOLD = 10 * 1024 * 1024; // 10MB

  public static FileReaderStrategist getStrategy(Path path) throws IOException {
    return Files.size(path) > LARGE_FILE_THRESHOLD
      ? new LazyReaderStrategy()
      : new EagerReaderStrategy();
  }
}
