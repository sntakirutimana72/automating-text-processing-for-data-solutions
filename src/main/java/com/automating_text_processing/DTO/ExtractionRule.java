package com.automating_text_processing.DTO;

import lombok.Getter;

import java.nio.file.Path;
import java.util.function.Consumer;

public class ExtractionRule extends ExtractableRule {
  @Getter private final Path source;
  @Getter private final Object writeLock;

  public ExtractionRule(Path source, Object writeLock, int version, String regex, PathLike destination, Consumer<ExtractionProgress> emitter) {
    super(version, regex, destination, emitter);

    this.source = source;
    this.writeLock = writeLock;
  }
}
