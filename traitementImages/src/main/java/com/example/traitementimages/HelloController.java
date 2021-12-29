package com.example.traitementimages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;

public class HelloController {
    private FilterImage filter  = new FilterImage("hello world");
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
    private ImageView imageView, blueRedGreen, blackAndWhite, sepia, prewitt;

    @FXML
    protected void initialize() {
        items.setItems(liste);
        fc = new FileChooser();
    }

    @FXML
    protected void chooseFile(ActionEvent actionEvent) throws IOException {
        File file = fc.showOpenDialog(stage);
        if (file != null) {
            liste.add(file.getName());
            items.setItems(liste);
            Path source = Paths.get(file.getAbsolutePath());
            Path dest = Paths.get("src/main/resources/images/" + file.getName());
            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
            Image image = new Image(dest.toFile().toURI().toURL().toString());
            imageView.setImage(image);
            blueRedGreen.setImage(image);
            blackAndWhite.setImage(image);
            sepia.setImage(image);
            prewitt.setImage(image);
        }
    }

    @FXML
    public void showImage() {
        String file = items.getSelectionModel().getSelectedItem();
        Path source = Paths.get("src/main/resources/images/" + file);
        Image image = new Image(source.toFile().toURI().toString());
        imageView.setImage(image);
        blueRedGreen.setImage(image);
        blackAndWhite.setImage(image);
        sepia.setImage(image);
        prewitt.setImage(image);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void loadImages() {
        File directory = new File("/src/main/resources/images/");
        for (File file : directory.listFiles()) {
            System.out.println(file.getName());
        }
        //commentaire test
    }
    public void direcoucou () {
        filter.coucou();
    }
}