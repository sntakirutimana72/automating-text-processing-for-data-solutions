package com.automating_text_processing.rule;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Iterator;

@EqualsAndHashCode(callSuper = true)
@Data
public class WriteRule extends PathLike {
  private Iterator<String> readIterator;
}
