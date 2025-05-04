package com.automating_text_processing.DTO;

import lombok.Getter;

import java.util.function.Consumer;

public class ReadRule extends PathLike {
  @Getter private final Consumer<String> lineConsumer;

  public ReadRule(String filename, Consumer<String> lineConsumer) {
    super();
    setFilename(filename);
    this.lineConsumer = lineConsumer;
  }
}
