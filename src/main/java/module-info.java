module com.automating_text_processing {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.web;

  requires org.controlsfx.controls;
  requires com.dlsc.formsfx;
  requires net.synedra.validatorfx;
  requires org.kordamp.ikonli.javafx;
  requires org.kordamp.bootstrapfx.core;
  requires static lombok;

  opens com.automating_text_processing to javafx.fxml;
  exports com.automating_text_processing;
  opens com.automating_text_processing.controller.editor to javafx.fxml;
  exports com.automating_text_processing.controller.editor;
  exports com.automating_text_processing.service;
  opens com.automating_text_processing.service to javafx.fxml;
  exports com.automating_text_processing.rule;
  exports com.automating_text_processing.function;
}
