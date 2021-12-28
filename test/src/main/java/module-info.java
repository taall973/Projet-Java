module com.example.exercice2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.exercice2 to javafx.fxml;
    exports com.example.exercice2;
}