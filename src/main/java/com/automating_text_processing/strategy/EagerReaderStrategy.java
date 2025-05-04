package com.automating_text_processing.strategy;

import com.automating_text_processing.rule.ReadRule;

import java.io.IOException;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

public class EagerReaderStrategy implements FileReaderStrategist {

  @Override
  public void read(ReadRule rule) throws IOException {
    Files.readAllLines(rule.getPath(), StandardCharsets.UTF_8).forEach(rule.getLineConsumer());
  }
}

