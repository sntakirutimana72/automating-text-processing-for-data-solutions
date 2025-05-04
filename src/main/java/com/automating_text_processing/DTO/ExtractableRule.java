package com.automating_text_processing.DTO;

import lombok.Data;

import java.util.function.Consumer;

@Data
public abstract class ExtractableRule {
  private final int version;
  private final String regex;
  private final PathLike destination;
  private final Consumer<ExtractionProgress> progressEmitter;
}
