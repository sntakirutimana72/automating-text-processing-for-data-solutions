package com.automating_text_processing.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class IndexController {
  // External references
  private Stage primaryStage;
  // UI
  @FXML private AnchorPane extractor;

  public void deferredInit(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  @FXML
  public void initialize() {
    Platform.runLater(() -> {
      Object extractorController = extractor.getProperties().get("controller");
      if (extractorController instanceof TextExtractorController)
        ((TextExtractorController) extractorController).deferredInit(primaryStage);
    });
  }
}
