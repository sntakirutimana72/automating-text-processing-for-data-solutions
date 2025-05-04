package com.automating_text_processing.rule;

import lombok.Data;

import java.nio.file.Path;
import java.nio.file.Paths;

@Data
public abstract class PathLike implements ReadableAndWritable {
  private String filename;

  @Override
  public Path getPath() { return Paths.get(this.getFilename()).toAbsolutePath(); }
}
