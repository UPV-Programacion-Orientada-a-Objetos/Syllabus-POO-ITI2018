package com.spolancom;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HelloWorld_css extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Text txt = new Text("Hello, world");
        txt.setId("text-hello");
        txt.relocate(115, 40);

        Button btn = new Button("Exit");
        btn.setStyle("-fx-text-fill: white; -fx-background-color: red");
        btn.relocate(155, 80);
        btn.setOnAction(actionEvent -> {
            System.out.println("Bye! see you later!");
            Platform.exit();
        });

        Pane pane = new Pane();
        pane.getChildren().addAll(txt, btn);

        Scene scene = new Scene(pane, 350, 150);
        scene.getStylesheets().add("/mystyle.css");

        stage.setTitle("The primary stage (top-level container");
        stage.onCloseRequestProperty().setValue(windowEvent -> System.out.println("\nBye! see you later!"));
        stage.setScene(scene);
        stage.show();
    }
}
