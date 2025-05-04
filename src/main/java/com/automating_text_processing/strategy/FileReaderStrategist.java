package com.automating_text_processing.strategy;

import com.automating_text_processing.DTO.ReadRule;

import java.io.IOException;

public interface FileReaderStrategist {
  void read(ReadRule rule) throws IOException;
}

