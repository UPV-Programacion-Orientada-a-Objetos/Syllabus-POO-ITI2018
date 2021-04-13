package com.spolancom;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.media.Media;

import java.io.File;

public class FxMediaPlayer extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    public void start1(Stage primaryStage) {

        Text txt = new Text("What a beautiful movie!");

        File f = new File("src/main/resources/sea.mp4");
        Media m = new Media(f.toURI().toString());
        MediaPlayer mp = new MediaPlayer(m);
        MediaView mv = new MediaView(mp);
        //mv.autosize();
        //mv.preserveRatioProperty();
        //mv.setFitHeight();
        //mv.setFitWidth();
        //mv.fitWidthProperty();
        //mv.fitHeightProperty()

        VBox vb = new VBox(txt, mv);
        vb.setSpacing(20);
        vb.setAlignment(Pos.CENTER);
        vb.setPadding(new Insets(10, 10, 10, 10));

        Scene scene = new Scene(vb, 650, 400);

        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX with embedded media player");
        primaryStage.onCloseRequestProperty()
                .setValue(e -> System.out.println("Bye! See you later!"));
        primaryStage.show();

        mp.play();
    }

    //@Override
    public void start(Stage primaryStage) {
        Text txt1 = new Text("What a beautiful music!");
        Text txt2 = new Text("If you don't hear music, turn up the volume.");

        File f = new File("src/main/resources/jb.mp3");
        System.out.println(f.toURI().toString());
        Media m = new Media(f.toURI().toString());
        MediaPlayer mp = new MediaPlayer(m);
        MediaView mv = new MediaView(mp);

        VBox vb = new VBox(txt1, txt2, mv);
        //VBox vb = new VBox(txt1, txt2);
        vb.setSpacing(20);;
        vb.setAlignment(Pos.CENTER);
        vb.setPadding(new Insets(10, 10, 10, 10));

        Scene scene = new Scene(vb, 350, 100);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFx with embeded media player");
        primaryStage.onCloseRequestProperty().setValue(event -> System.out.println("Bye! See you later!"));
        primaryStage.show();

        mp.play();
    }
}
