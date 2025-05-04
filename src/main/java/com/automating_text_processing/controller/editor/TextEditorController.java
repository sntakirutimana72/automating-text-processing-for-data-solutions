package com.automating_text_processing.controller.editor;

import com.automating_text_processing.service.TextEditorProcessingService;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;

import java.util.*;

public class TextEditorController {
  // Service & other operations
  @Getter private final TextEditorProcessingService service;
  @Setter @Getter private boolean replace;
  // Functionality controller
  @FXML private WebView webView;
  @FXML private AnchorPane editorMenu;

  public TextEditorController() {
    service = new TextEditorProcessingService();
    setReplace(false);
  }

  public void reset() {
    setContent("");
  }

  public WebEngine webEngine() {
    return webView.getEngine();
  }

  public void setContent(String data) {
    if (data != null) {
      Platform.runLater(() -> webEngine().executeScript(String.format("setContent('%s');", data)));
    }
  }

  public void addLine(@NonNull String line) {
    Platform.runLater(() -> webEngine().executeScript(String.format("addLine('%s');", line)));
  }

  public String getContent() {
    return (String) webEngine().executeScript("getContent();");
  }

  public Iterator<String> getContentIterator() {
    Object content = getContent();
    if (content instanceof String text) {
      return Arrays.stream(text.split("\\R")).iterator();
    }
    return Collections.emptyIterator();
  }

  @FXML
  public void initialize() {
    Platform.runLater(() -> {
      webEngine().load(Objects.requireNonNull(
        getClass()
          .getResource("/com/automating_text_processing/editor.html"))
          .toExternalForm());
      Object controller = editorMenu.getProperties().get("controller");

      if (controller instanceof FunctionalController)
        ((FunctionalController) controller).init(this, webView.getScene().getWindow());
      else
        System.err.println("FunctionalController was not loaded");
    });
  }
}
