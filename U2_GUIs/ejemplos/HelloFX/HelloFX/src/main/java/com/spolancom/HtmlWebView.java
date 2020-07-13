package com.spolancom;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.stage.Stage;
import javafx.scene.web.WebView;

import java.io.File;
import java.util.Date;

public class HtmlWebView extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        System.out.println("Doing what has to be done before closing");
    }

    @Override
    public void start(Stage primaryStage) {

        //this.simpleHTML(primaryStage);
        //this.FxWithEmbeddedHtml(primaryStage);
        // this.HtmlForm(primaryStage);
        this.googleSearch(primaryStage);
    }

    private void googleSearch(Stage primaryStage) {
        Text txt = new Text("Enjoy searching the web!");

        WebView wv = new WebView();
        WebEngine we = wv.getEngine();
        we.load("http://www.google.com");

        // wv.setZoom(1.5);
        // wv.setFontScale(1.5);


//        WebHistory history = we.getHistory();
//        ObservableList<WebHistory.Entry> entries = history.getEntries();
//        for(WebHistory.Entry entry: entries){
//            String url = entry.getUrl();
//            String title = entry.getTitle();
//            Date date = entry.getLastVisitedDate();
//        }


        VBox vb = new VBox(txt, wv);
        vb.setSpacing(20);
        vb.setAlignment(Pos.CENTER);
        vb.setStyle("-fx-font-size: 20px; -fx-background-color: lightblue;");
        vb.setPadding(new Insets(10, 10, 10, 10));

        Scene scene = new Scene(vb, 750, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Java with the window to another server");
        primaryStage.onCloseRequestProperty().setValue(event -> System.out.println("Bye! See you later!"));
        primaryStage.show();
    }

    private void HtmlForm(Stage primaryStage) {
        Text txt = new Text("Fill the form and click Submit");

        WebView wv = new WebView();
        WebEngine we = wv.getEngine();
        File f = new File("src/main/resources/form.html");
        we.load(f.toURI().toString());

        VBox vb = new VBox(txt, wv);
        vb.setSpacing(10);
        vb.setAlignment(Pos.CENTER);
        vb.setPadding(new Insets(10, 10, 10, 10));

        Scene scene = new Scene(vb, 300, 200);

        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX with embedded HTML");
        primaryStage.onCloseRequestProperty().setValue(event -> System.out.println("Bye! See you later!"));
        primaryStage.show();
    }

    private void FxWithEmbeddedHtml(Stage primaryStage) {
        try {
            WebView wv = new WebView();
            WebEngine we = wv.getEngine();
            String html = "<html><center><h2>Hello, world!</h2></center></html>";
            we.loadContent(html, "text/html");

            Text txt = new Text("Below is the embedded HTML");

            VBox vb = new VBox(txt, wv);
            vb.setSpacing(10);
            vb.setAlignment(Pos.CENTER);
            vb.setPadding(new Insets(10, 10, 10, 10));

            Scene scene = new Scene(vb, 300, 120);

            primaryStage.setTitle("My HTML page");
            primaryStage.setScene(scene);
            primaryStage.onCloseRequestProperty().setValue(event -> System.out.println("Bye! See you later!"));
            primaryStage.show();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void simpleHTML(Stage primaryStage) {

        try {
            Text txt = new Text("My First web page");

            WebView wv = new WebView();
            WebEngine we = wv.getEngine();
            String html = "<html><center><h2>Hello, world!</h2></center></html>";
            we.loadContent(html, "text/html");

            VBox vb = new VBox(txt, wv);
            vb.setSpacing(10);
            vb.setAlignment(Pos.CENTER);
            vb.setPadding(new Insets(10, 10, 10, 10));

            Scene scene = new Scene(vb, 300, 120);
            //Scene scene = new Scene(wv, 200, 60);
            primaryStage.setTitle("My HTML page");
            primaryStage.setScene(scene);
            primaryStage.onCloseRequestProperty().setValue(event -> System.out.println("Bye! See you later!"));
            primaryStage.show();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
