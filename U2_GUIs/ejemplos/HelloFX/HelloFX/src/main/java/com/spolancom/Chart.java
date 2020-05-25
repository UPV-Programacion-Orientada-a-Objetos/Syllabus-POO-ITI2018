package com.spolancom;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Chart extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        Text txt = new Text("Test results: ");

        PieChart pc = new PieChart();
        pc.getData().add(new PieChart.Data("Succeed", 143));
        pc.getData().add(new PieChart.Data("Failed", 12));
        pc.getData().add(new PieChart.Data("Ignored", 18));

        VBox vb = new VBox(txt, pc);
        vb.setAlignment(Pos.CENTER);
        vb.setPadding(new Insets(10, 10, 10, 10));

        primaryStage.setTitle("A chart example");
        primaryStage.onCloseRequestProperty().setValue(windowEvent -> System.out.println("Bye! See you later!"));
        primaryStage.setScene(new Scene(vb, 300, 300));
        primaryStage.show();
    }
}
