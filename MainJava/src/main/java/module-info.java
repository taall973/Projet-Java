module com.example.traitementimages {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;

    opens com.example.traitementimages to javafx.fxml;
    exports com.example.traitementimages;
}