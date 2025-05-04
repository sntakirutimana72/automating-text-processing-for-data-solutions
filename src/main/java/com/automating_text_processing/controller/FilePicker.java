package com.automating_text_processing.controller;

import com.automating_text_processing.DTO.FilePickerFilterDTO;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class FilePicker {
  private FilePicker() {}

  private static FileChooser defineChooser(String title, List<FilePickerFilterDTO> filters) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle(title);
    filters.forEach(filter -> fileChooser.getExtensionFilters().add(
      new FileChooser.ExtensionFilter(filter.label(), filter.fileExtension())
    ));
    return fileChooser;
  }

  public static File pickFile(String title, List<FilePickerFilterDTO> filters, Stage stage) {
    return defineChooser(title, filters).showOpenDialog(stage);
  }

  public static List<File> pickFiles(String title, List<FilePickerFilterDTO> filters, Stage stage) {
    return defineChooser(title, filters).showOpenMultipleDialog(stage);
  }

  public static File pickLocation(String title, Stage stage) {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle(title);
    return directoryChooser.showDialog(stage);
  }
}
