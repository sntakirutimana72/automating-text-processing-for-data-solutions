package com.automating_text_processing.DTO;

import lombok.Data;

import java.nio.file.Path;
import java.nio.file.Paths;

@Data
public class PathLike implements ReadableAndWritable {
  private String filename;

  public PathLike(String filename) {
    setFilename(filename);
  }

  public PathLike() {}

  @Override
  public Path getPath() { return Paths.get(this.getFilename()).toAbsolutePath(); }
}
