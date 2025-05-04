package com.automating_text_processing.rule;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.function.Consumer;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReadRule extends PathLike {
  private Consumer<String> lineConsumer;
}
