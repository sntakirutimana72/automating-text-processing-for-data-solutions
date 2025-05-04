package com.automating_text_processing.controller;

import com.automating_text_processing.DTO.ExtractorRule;
import com.automating_text_processing.DTO.FilePickerFilterDTO;
import com.automating_text_processing.DTO.PathLike;
import com.automating_text_processing.DTO.SearchPatterns;
import com.automating_text_processing.logger.Alert;
import com.automating_text_processing.service.TextExtractorService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TextExtractorController {
  // External references
  private Stage rootStage;
  // UI
  @FXML private AnchorPane rootContainer;
  @FXML private ListView<SearchPatterns> predefinedSearchPatternListView;
  @FXML private VBox customSearchPatternListView;
  @FXML private VBox sourcesListView;

  // Service manager
  TextExtractorService extractorService;

  public TextExtractorController() {
    extractorService = new TextExtractorService();
  }

  public void deferredInit(Stage rootStage) {
    this.rootStage = rootStage;
  }

  private void loadPredefinedSearchPatterns() {
    predefinedSearchPatternListView.getItems().addAll(SearchPatterns.values());
    predefinedSearchPatternListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  @FXML
  private void handleSourcesSelection() {
    // Show the file chooser and allow multiple selection
    List<File> sources = FilePicker.pickFiles("Select sources", List.of(
      new FilePickerFilterDTO("Text Files", "*.txt"),
      new FilePickerFilterDTO("Log Files", "*.log")
    ), rootStage);

    if (Objects.nonNull(sources))
      sources.forEach(file -> {
        Label label = new Label(file.getAbsolutePath());
        Button remove = new Button("X");
        HBox sourceItem = new HBox(5, label, remove);

        sourceItem.setStyle("-fx-padding: 10; -fx-border-color: gray;");
        sourcesListView.getChildren().add(sourceItem);
        remove.setOnAction(e -> sourcesListView.getChildren().remove(sourceItem));
      });
  }

  @FXML
  private void handleNewCustomSearchPattern() {
    TextField field = new TextField();
    field.setPromptText("Enter custom search pattern");
    field.setStyle("-fx-margin: 0 5px 0 0;");
    Button remove = new Button("X");
    HBox customSearchPatternItem = new HBox(0, field, remove);

    customSearchPatternItem.setPrefHeight(20);
    customSearchPatternItem.getStyleClass().add("layout");
    customSearchPatternListView.getChildren().add(customSearchPatternItem);
    remove.setOnAction(e -> customSearchPatternListView
      .getChildren().remove(customSearchPatternItem));
  }

  @FXML
  private void handleSubmitTask() {
    List<String> regex = new java.util.ArrayList<>(predefinedSearchPatternListView.getSelectionModel()
      .getSelectedItems().stream().map(SearchPatterns::toString).toList());
    customSearchPatternListView.getChildren()
      .forEach(it -> ((HBox) it).getChildren().forEach((c) -> {
      if (c instanceof TextField)
        regex.add(((TextField) c).getText());
    }));
    // Abort if regex list is empty
    if (regex.isEmpty()) {
      Alert.info("Extractor - Task Submission", null, "No search pattern provided");
      return;
    }

    // Abort immediately if no sources provided
    List<PathLike> sources = new ArrayList<>(List.of());
    sourcesListView.getChildren().forEach(it -> ((HBox)it).getChildren().forEach(c -> {
      if (c instanceof Label)
        sources.add(new PathLike(((Label) c).getText()));
    }));
    if (sources.isEmpty()) {
      Alert.info("Extractor - Task Submission", null, "No sources found");
      return;
    }

    // Define extraction rule
    ExtractorRule rule = new ExtractorRule(
      extractorService.newExtractionVersion(),
      sources,
      "(?i)" + regex.stream().map(pattern -> "(" + pattern + ")").collect(Collectors.joining("|")),
      new PathLike("/home/stevie/Downloads/report.txt"),
      (emission) -> Platform.runLater(() -> System.out.println(emission))
    );
    extractorService.extract(rule);
    resetTaskSubmission();
  }

  @FXML
  private void resetTaskSubmission() {
//    destination.setText("");
    sourcesListView.getChildren().clear();
    customSearchPatternListView.getChildren().clear();
    predefinedSearchPatternListView.getSelectionModel().clearSelection();
  }

  @FXML
  public void initialize() {
    rootContainer.getProperties().put("controller", this);
    Platform.runLater(this::loadPredefinedSearchPatterns);
  }
}
