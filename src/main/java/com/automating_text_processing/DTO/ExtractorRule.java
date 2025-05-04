package com.automating_text_processing.DTO;

import lombok.Getter;

import java.util.List;
import java.util.function.Consumer;

public class ExtractorRule extends ExtractableRule {
  @Getter private final List<PathLike> sources;

  public ExtractorRule(int version, List<PathLike> sources, String regex, PathLike destination, Consumer<ExtractionProgress> emitter) {
    super(version, regex, destination, emitter);
    this.sources = sources;
  }
}
