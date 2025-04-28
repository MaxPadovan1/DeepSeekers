module com.example.teach {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.teach to javafx.fxml;
    exports com.example.teach;
    exports com.example.teach.controller;
    opens com.example.teach.controller to javafx.fxml;
}