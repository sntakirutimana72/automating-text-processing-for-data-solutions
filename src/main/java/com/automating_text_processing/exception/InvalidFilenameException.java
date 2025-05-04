package com.automating_text_processing.exception;

import java.util.Objects;

public class InvalidFilenameException extends RuntimeException {
  public InvalidFilenameException(String message) {
    super(Objects.isNull(message) ? "Please provide filename before saving" :  message);
  }
}
