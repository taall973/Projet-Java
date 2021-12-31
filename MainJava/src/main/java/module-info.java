module com.example.traitementimages {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires jakarta.xml.bind;
    requires com.sun.xml.bind;

    opens com.example.traitementimages to jakarta.xml.bind, javafx.fxml;
    exports com.example.traitementimages;
}