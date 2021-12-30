module com.example.traitementimages {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.traitementimages to javafx.fxml;
    exports com.example.traitementimages;
}