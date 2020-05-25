package com.spolancom;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class FxImage extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Text txt = new Text("What a beautiful image!");

        FileInputStream input = null;
        try {
            input = new FileInputStream("src/main/resources/packt.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Image image = new Image(input);
        ImageView iv = new ImageView(image);

        VBox vb = new VBox(txt, iv);
        vb.setSpacing(20);
        vb.setAlignment(Pos.CENTER);
        vb.setPadding(new Insets(10, 10, 10, 10));

        Scene scene = new Scene(vb, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX with embedded image");
        primaryStage.onCloseRequestProperty().set(event -> System.out.println("Bye! See you later"));
        primaryStage.show();
    }
}
