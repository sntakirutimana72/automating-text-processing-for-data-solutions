package com.automating_text_processing;

import com.automating_text_processing.controller.IndexController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AutomatingTextProcessingApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
      FXMLLoader fxmlLoader = new FXMLLoader(AutomatingTextProcessingApplication.class.getResource("index.fxml"));
      Scene scene = new Scene(fxmlLoader.load(), 600, 400);
      IndexController controller = fxmlLoader.getController();

      controller.deferredInit(stage);
      stage.setTitle("Automating Text Processing For Dataflows Solutions");
      stage.setScene(scene);
      stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
