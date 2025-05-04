package com.automating_text_processing.DTO;

import java.nio.file.StandardOpenOption;
import java.util.Iterator;

public class ExtractorWriteRule extends WriteRule {
  public ExtractorWriteRule(Iterator<String> readIterator) {
    super(readIterator, StandardOpenOption.APPEND);
  }
}
