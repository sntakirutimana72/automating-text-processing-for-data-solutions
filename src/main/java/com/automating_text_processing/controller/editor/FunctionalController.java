package com.automating_text_processing.controller.editor;

import com.automating_text_processing.rule.EditorFindRule;
import com.automating_text_processing.util.WebUtils;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

public class FunctionalController {
  @FXML private AnchorPane menuRoot;
  @FXML private TextField replacementField;
  @FXML private TextField targetField;
  @FXML private TextField filenameField;
  @FXML private Button replaceButton;
  @FXML private CheckBox replaceToggler;

  // Current open file
  @Getter @Setter private String currentOpenFile;

  private TextEditorController editor;
  private Window window;

  public void init(TextEditorController editor, Window window) {
    this.editor = editor;
    this.window = window;
  }

  // Reset editor to default
  @FXML
  private void handleReset() {
    editor.reset();
    filenameField.clear();
    setCurrentOpenFile("");
  }

  // Open existing file
  @FXML
  private void handleOpen() {
    handleReset();
    ReadAndWriteFunctionality.onOpen(
      window,
      filenameField::setText,
      this::setCurrentOpenFile,
      (line) -> editor.addLine(WebUtils.escaped(line)),
      editor.getService()::read);
  }

  // Save existing or new file
  @FXML
  private void handleSave() {
    ReadAndWriteFunctionality.onSave(
      filenameField.getText().trim(),
      window,
      this::getCurrentOpenFile,
      this::setCurrentOpenFile,
      editor::getContentIterator, editor.getService()::write);
  }

  private void toggleReplacement(boolean state) {
    replacementField.setManaged(state);
    replacementField.setVisible(state);
    replacementField.clear();
    replaceButton.setManaged(state);
    replaceButton.setVisible(state);
  }

  private EditorFindRule defineFindAndReplaceAllRule() {
    EditorFindRule rule = new EditorFindRule(() -> WebUtils.escaped(editor.getContent()));
    // Ready action rule
    rule.setTarget(targetField.getText());
    rule.setReplace(editor.isReplace());
    rule.setReplacement(replacementField.getText());

    return rule;
  }

  // Handle Find & Replace functionalities
  private void handleFind() {
    PauseTransition delay = new PauseTransition(Duration.millis(600));
    targetField.textProperty().addListener((obs, oldVal, newVal) -> {
      // Restart the delay on every input
      delay.stop();
      delay.setOnFinished(evt -> editor.getService()
          .highlight(defineFindAndReplaceAllRule()).thenAccept(editor::setContent));
      delay.play();
    });
  }

  // Handle Find & Replace functionalities
  @FXML
  private void handleReplaceAll() {
    if (targetField.getText().isEmpty() || replacementField.getText().isEmpty()) return;
    // Engage service worker
    editor.getService().replace(defineFindAndReplaceAllRule()).thenAccept(editor::setContent);
  }

  @FXML
  public void initialize() {
    setCurrentOpenFile("");
    menuRoot.getProperties().put("controller", this);
    toggleReplacement(false);
    replaceToggler.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
      editor.setReplace(isSelected);
      toggleReplacement(isSelected);
    });
    handleFind();
  }
}
