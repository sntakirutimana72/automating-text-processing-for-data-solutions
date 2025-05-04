package com.automating_text_processing.exception;

public class ExtractionBatchNotFound extends Exception {
  public ExtractionBatchNotFound(int version) {
    super("No extraction batch with ID=" + version + " found");
  }
}
