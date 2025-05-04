package com.automating_text_processing.strategy;

import com.automating_text_processing.rule.ReadRule;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;

public class LazyReaderStrategy implements FileReaderStrategist {

  @Override
  public void read(ReadRule rule) throws IOException {
    try (BufferedReader reader = Files.newBufferedReader(rule.getPath())) {
      String line;
      while ((line = reader.readLine()) != null) {
        rule.getLineConsumer().accept(line);
      }
    }
  }
}
