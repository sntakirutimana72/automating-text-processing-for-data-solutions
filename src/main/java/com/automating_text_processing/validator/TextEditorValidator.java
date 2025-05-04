package com.automating_text_processing.validator;

import com.automating_text_processing.exception.InvalidFilenameException;

import lombok.NonNull;

public class TextEditorValidator {
  public static String validateFilenameOnSave(@NonNull String name) {
    if (name.isBlank())
      throw new InvalidFilenameException(null);
    if (name.matches("^[a-zA-Z0-9]+([-_]*[a-zA-Z0-9])*$"))
      return name;
    throw new InvalidFilenameException("Filename can only contain letters, digits, hyphen, and underscore");
  }
}
