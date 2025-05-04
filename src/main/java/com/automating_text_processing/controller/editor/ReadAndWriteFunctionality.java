package com.automating_text_processing.controller.editor;

import com.automating_text_processing.function.RuleConsumer;
import com.automating_text_processing.logger.Alert;
import com.automating_text_processing.rule.ReadRule;
import com.automating_text_processing.rule.WriteRule;
import com.automating_text_processing.validator.TextEditorValidator;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javafx.stage.FileChooser;
import javafx.stage.Window;

public class ReadAndWriteFunctionality {
  private static WriteRule defineWriteRule(String filename, Supplier<Iterator<String>> reader) {
    WriteRule rule = new WriteRule();
    rule.setFilename(filename);
    rule.setReadIterator(reader.get());
    return rule;
  }

  private static ReadRule defineReadRule(File file, Consumer<String> consumer) {
    ReadRule rule = new ReadRule();

    rule.setLineConsumer(consumer);
    rule.setFilename(file.getAbsolutePath());

    return rule;
  }

  private static String buildSaveAbsolutePath(String name, String currentOpenFilename, Window win) {
    // Check if we have a current open filename
    if (currentOpenFilename.equalsIgnoreCase(name + ".txt"))
      // Check if the current open filename match the provided name
      return currentOpenFilename;
      // No current open file, but given name contains file separator, in other words, could be a full path
    if (name.contains(FileSystems.getDefault().getSeparator()))
      return name + ".txt";
    // Otherwise, build a file path
    String stem = TextEditorValidator.validateFilenameOnSave(name.trim());
    // Select location, i.e. where to save your file
    // Initiate directory chooser
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Save Location");
    // If no location was selected, abort
    File location = directoryChooser.showDialog(win);
    if (Objects.isNull(location)) return null;
    // Combine location with given name, and then add suffix
    return location.toPath().resolve(stem + ".txt").toAbsolutePath().toString();
  }

  public static void onSave(String name, Window win, Supplier<String> getOpenFilename,
                            Consumer<String> setCurrentOpenFilename,
                            Supplier<Iterator<String>> reader, RuleConsumer<WriteRule> writer) {
    try {
      String filename;
      if ((filename = buildSaveAbsolutePath(name, getOpenFilename.get(), win)) == null) return;
      // Define write rule
      WriteRule rule = defineWriteRule(filename, reader);

      // if editor mode isn't overwrite and the absolute path points to an existing file
      if (rule.getPath().toFile().exists() &&
        !rule.getFilename().equalsIgnoreCase(getOpenFilename.get()) &&
        !Alert.confirmation("Overwrite?", null, String.format("""
          File:%s already exists.
          Are you sure you want to overwrite it?
          """, rule.getFilename())))
        return;

      writer.accept(rule);
      // Update current open filename
      setCurrentOpenFilename.accept(rule.getFilename());
    }
    catch (Exception e) {
      Alert.error("Save File", null, e.getMessage());
    }
  }

  public static void onOpen(Window win, Consumer<String> setName, Consumer<String> setOpenFile,
                            Consumer<String> addLine, RuleConsumer<ReadRule> reader) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Text File");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

    File file = fileChooser.showOpenDialog(win);
    if (!Objects.isNull(file)) {
      ReadRule rule = defineReadRule(file, addLine);
      try {
        reader.accept(rule);
        // Set the original filename
        setName.accept(rule.getFilename().substring(0, rule.getFilename().lastIndexOf(".")));
        setOpenFile.accept(rule.getFilename());
        // Set write rule as OVERRIDE
      } catch (Exception e) {
        Alert.error("Open File", null, e.getMessage());
      }
    }
  }
}
