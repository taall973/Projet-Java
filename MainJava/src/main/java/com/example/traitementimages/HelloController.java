package com.example.traitementimages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;

public class HelloController {

    private FilterImage filterImage;
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
    private Button vInv;

    @FXML
    protected void initialize() {
        loadImages();
        fc = new FileChooser();
    }

    @FXML
    protected void chooseFile(ActionEvent actionEvent) throws IOException {
        File file = fc.showOpenDialog(stage);
        if (file != null) {
            boolean unique = true;
            for (String elmt : liste) {
                if (elmt.equals(file.getName())) {
                    Alert sameName = new Alert(Alert.AlertType.INFORMATION, "Un fichier enregistré à ce nom existe déjà !");
                    sameName.setTitle("Doublon détecté");
                    sameName.setHeaderText(null);
                    sameName.show();
                    unique = false;
                }
            }
            if (unique) {
                liste.add(file.getName());
                items.setItems(liste);
                Path source = Paths.get(file.getAbsolutePath());
                Path dest = Paths.get("src/main/resources/images/" + file.getName());
                Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
                Image image = new Image(dest.toFile().toURI().toURL().toString());
                filterImage = new FilterImage(image);
                imageView.setImage(filterImage.getImage());
                blueRedGreen.setImage(filterImage.toBRG());
                blackAndWhite.setImage(filterImage.toBlackAndWhite());
                sepia.setImage(filterImage.toSepia());
                prewitt.setImage(filterImage.toPrewitt());
            } else {
                items.getSelectionModel().select(file.getName());
                showImage();
            }
        }
    }

    @FXML
    public void showImage() {
        Image image = getCurrentImage();
        filterImage = new FilterImage(image);
        imageView.setImage(image);
        blueRedGreen.setImage(filterImage.toBRG());
        blackAndWhite.setImage(filterImage.toBlackAndWhite());
        sepia.setImage(filterImage.toSepia());
        prewitt.setImage(filterImage.toPrewitt());
    }

    @FXML
    public void verticalInvert() {
        Image image = getCurrentImage();
        filterImage = new FilterImage(image);
        imageView.setImage(filterImage.bottomToTop());
    }

    @FXML
    public void blueRedGreenFilter() {
        Image currentImage = getCurrentImage();
        filterImage = new FilterImage(currentImage);
        imageView.setImage(filterImage.toBRG());
    }

    @FXML
    public void blackAndWhiteFilter() {
        Image currentImage = getCurrentImage();
        filterImage = new FilterImage(currentImage);
        imageView.setImage(filterImage.toBlackAndWhite());
    }

    @FXML
    public void sepiaFilter() {
        Image currentImage = getCurrentImage();
        filterImage = new FilterImage(currentImage);
        imageView.setImage(filterImage.toSepia());
    }

    @FXML
    public void prewittFilter() {
        Image currentImage = getCurrentImage();
        filterImage = new FilterImage(currentImage);
        imageView.setImage(filterImage.toPrewitt());
    }

    public Image getCurrentImage() {
        String file = items.getSelectionModel().getSelectedItem();
        Path source = Paths.get("src/main/resources/images/" + file);
        Image image = new Image(source.toFile().toURI().toString());
        return image;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void loadImages() {
        File directory = Paths.get("src/main/resources/images/").toFile();
        for (File file : directory.listFiles()) {
            liste.add(file.getName());
        }
        items.setItems(liste);
    }

}