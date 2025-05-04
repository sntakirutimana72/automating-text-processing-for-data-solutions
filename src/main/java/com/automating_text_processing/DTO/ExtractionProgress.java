package com.automating_text_processing.DTO;

import lombok.Data;

@Data
public class ExtractionProgress {
  private final Integer version;
  private Double progress;
  private String source;
  private ExtractionStatus status;
  private String error;

  public ExtractionProgress(int version, double progress, String source, ExtractionStatus status, String error) {
    this.version = version;
    setProgress(progress);
    setSource(source);
    setStatus(status);
    setError(error);
  }
}
