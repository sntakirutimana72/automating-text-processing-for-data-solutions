package com.automating_text_processing.service;

import com.automating_text_processing.DTO.ReadRule;
import com.automating_text_processing.DTO.WriteRule;
import com.automating_text_processing.strategy.FileReaderContext;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public abstract class TextProcessingCoreService {

  public void read(ReadRule rule) throws IOException {
    // Get the right reading strategy based on file size, and then read & consume data
    FileReaderContext.getStrategy(rule.getPath()).read(rule);
  }

  public void write(WriteRule rule) throws IOException {
    try (BufferedWriter writer = Files.newBufferedWriter(rule.getPath(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, rule.getExtraOpenOption())) {
      while (rule.getReadIterator().hasNext()) {
        writer.write(rule.getReadIterator().next());
        writer.newLine();
      }
    }
  }
}
