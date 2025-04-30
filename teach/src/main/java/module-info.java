module com.example.teach {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports com.example.teach;
    exports com.example.teach.model;
    exports com.example.teach.controller;
    opens com.example.teach.controller to javafx.fxml;
}
