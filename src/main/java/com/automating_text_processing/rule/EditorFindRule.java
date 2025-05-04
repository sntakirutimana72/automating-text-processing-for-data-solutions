package com.automating_text_processing.rule;

import lombok.Getter;

import java.util.function.Supplier;

public class EditorFindRule extends FindRule {
  @Getter private final Supplier<String> reader;

  public EditorFindRule(Supplier<String> reader) {
    this.reader = reader;

    setFilename(null);
  }
}
