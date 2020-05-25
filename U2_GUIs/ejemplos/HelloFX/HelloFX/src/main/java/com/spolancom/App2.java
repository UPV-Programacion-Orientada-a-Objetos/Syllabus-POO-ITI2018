package com.spolancom;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class App2 extends Application {

    public void start(Stage primaryStage) {
        Text txt = new Text("Fill the form and click submit");
        TextField tfFirstName = new TextField();
        TextField tfLastName = new TextField();
        TextField tfAge = new TextField();
        Button btn = new Button("Submit");
        btn.setOnAction(actionEvent -> action(tfFirstName, tfLastName, tfAge));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(15);
        grid.setVgap(5);
        grid.setPadding(new Insets(20, 20, 20, 20));

        int i = 0;
        grid.add(txt, 1, i++, 2, 1);

        GridPane.setHalignment(txt, HPos.CENTER);
        grid.addRow(i++, new Label("First Name"), tfFirstName);
        grid.addRow(i++, new Label("Last Name"), tfLastName);
        grid.addRow(i++, new Label("Age"), tfAge);
        grid.add(btn, 1, i);

        grid.setGridLinesVisible(true);

        GridPane.setHalignment(btn, HPos.CENTER);

        primaryStage.setTitle("Simple form example");
        primaryStage.onCloseRequestProperty().setValue(windowEvent -> System.out.println("Bye! See you later!"));
        primaryStage.setScene(new Scene(grid, 300, 200));
        primaryStage.show();
    }

    private void action(TextField tfFirstName, TextField tfLastName, TextField tfAge) {
        String fn = tfFirstName.getText();
        String ln = tfLastName.getText();
        String age = tfAge.getText();
        int a = 42;
        try {
            a = Integer.parseInt(age);
        }
        catch (Exception e) {

        }
        fn = fn.isBlank() ? "Nick" : fn;
        ln = ln.isBlank() ? "Samoylov" : ln;
        System.out.println("Hello, " + fn + " " + ln + ", age: " + a + "!");
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
