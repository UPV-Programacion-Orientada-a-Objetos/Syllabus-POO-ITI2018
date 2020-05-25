module com.spolancom {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.media;
    opens com.spolancom to javafx.fxml, javafx.media;
    exports com.spolancom;
}