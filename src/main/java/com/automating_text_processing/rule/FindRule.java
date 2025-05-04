package com.automating_text_processing.rule;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class FindRule extends PathLike {
  private boolean replace;
  private String replacement;
  private String target;
}
