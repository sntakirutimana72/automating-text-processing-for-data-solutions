package com.automating_text_processing.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.nio.file.StandardOpenOption;
import java.util.Iterator;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class WriteRule extends PathLike {
  private final Iterator<String> readIterator;
  private final StandardOpenOption extraOpenOption;
}
