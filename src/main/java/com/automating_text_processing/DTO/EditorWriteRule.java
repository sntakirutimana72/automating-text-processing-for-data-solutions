package com.automating_text_processing.DTO;

import java.nio.file.StandardOpenOption;
import java.util.Iterator;

public class EditorWriteRule extends WriteRule {
  public EditorWriteRule(String filename, Iterator<String> readIterator) {
    super(readIterator, StandardOpenOption.TRUNCATE_EXISTING);
    setFilename(filename);
  }
}
