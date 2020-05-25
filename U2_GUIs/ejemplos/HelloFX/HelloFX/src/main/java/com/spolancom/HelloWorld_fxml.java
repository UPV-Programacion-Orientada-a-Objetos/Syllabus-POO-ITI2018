package com.spolancom;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.net.URL;

public class HelloWorld_fxml  extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new URL("file:src/main/resources/helloWorld.fxml"));
            Scene scene = loader.load();

            primaryStage.setTitle("simple form example");
            primaryStage.setScene(scene);
            primaryStage.onCloseRequestProperty().setValue(event -> System.out.println("\nBye! see you later!"));
            primaryStage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
