module com.example.teach {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.teach to javafx.fxml;
    exports com.example.teach;
}