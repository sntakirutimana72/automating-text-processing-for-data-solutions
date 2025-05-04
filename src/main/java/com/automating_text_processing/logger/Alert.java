package com.automating_text_processing.logger;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

import java.util.Optional;

public class Alert {
  private static Optional<ButtonType> alert(AlertType level, String title, String header, String message) {
    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(level);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(message);

    return alert.showAndWait();
  }

  public static Boolean confirmation(String title, String header, String message) {
    Optional<ButtonType> decision = alert(AlertType.CONFIRMATION, title, header, message);
    return decision.isPresent() && decision.get() == ButtonType.OK;
  }

  public static void error(String title, String header, String message) {
    alert(AlertType.ERROR, title, header, message);
  }

  public static void info(String title, String header, String message) {
    alert(AlertType.INFORMATION, title, header, message);
  }
}
