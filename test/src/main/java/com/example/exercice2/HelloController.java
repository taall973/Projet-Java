package com.example.exercice2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;

public class HelloController {

    @FXML
    BorderPane bp;

    Stage stage;
    @FXML
    private FileChooser fc;
    @FXML
    private MenuItem pickImage;
    private ObservableList<String> liste = FXCollections.observableArrayList();
    @FXML
    private ListView<String> items;
    @FXML
    private TextField text;

    @FXML
    protected void initialize() {
        items.setItems(liste);
        fc = new FileChooser();
    }

    @FXML
    protected void chooseFile(ActionEvent actionEvent) throws IOException {
        File file = fc.showOpenDialog(stage);
        if (file != null) {
            liste.add(file.getAbsolutePath());
            liste.add((Paths.get("").toAbsolutePath()).toString());
            liste.add((FileSystems.getDefault().getPath("src\\main\\resources\\images").toAbsolutePath()).toString());
            items.setItems(liste);
            Path source = Paths.get(file.getAbsolutePath());

            Path dest = Paths.get("src/main/resources/images/" + file.getName());
            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


}