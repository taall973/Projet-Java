package com.example.traitementimages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

public class HelloController {

    private RegisteredImages filterImage;
    Stage stage;
    private int rotate; //variable qui contient la valeur de la rotation de l'image affichée
    @FXML
    private FileChooser fc;
    @FXML
    private MenuItem pickImage;
    private ObservableList<String> liste = FXCollections.observableArrayList();
    private ObservableList<String> tagList = FXCollections.observableArrayList();
    private ObservableList<String> imageTagList = FXCollections.observableArrayList();
    @FXML
    private ListView<String> items, itemsTag, imageTags;
    @FXML
    private TextField searchTag, addTag;
    @FXML
    private ImageView imageView, blueRedGreen, blackAndWhite, sepia, prewitt;
    @FXML
    private Button vInv;
    private ArrayList<RegisteredImages> images;

    @FXML
    protected void initialize() {
        images = new ArrayList<>();
        rotation(0);
        loadImages();
        fc = new FileChooser();
        searchTag.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    if (!(tagList.contains(searchTag.getText()))) {
                        tagList.add(searchTag.getText());
                        itemsTag.setItems(tagList);
                        imagesWithTags();
                    }
                }
            }
        });
        addTag.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    if (!(imageTagList.contains(addTag.getText()))) {
                        images.get(items.getSelectionModel().getSelectedIndex()).addTag(addTag.getText());
                        imageTagList.add(addTag.getText());
                        imageTags.setItems(imageTagList);
                    }
                }
            }
        });
    }

    @FXML
    protected void chooseFile() throws IOException {
        File file = fc.showOpenDialog(stage);
        if (file != null) {
            if (liste.contains(file.getName())) {
                Alert sameName = new Alert(Alert.AlertType.INFORMATION, "Un fichier enregistré à ce nom existe déjà !");
                sameName.setTitle("Doublon détecté");
                sameName.setHeaderText(null);
                sameName.show();
                items.getSelectionModel().select(file.getName());
                showImage();
            } else {
                rotation(0);
                liste.add(file.getName());
                items.setItems(liste);
                Path source = Paths.get(file.getAbsolutePath());
                Path dest = Paths.get("src/main/resources/images/" + file.getName());
                Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
                Image image = new Image(dest.toFile().toURI().toURL().toString());
                filterImage = new RegisteredImages(image);
                imageView.setImage(filterImage.getImage());
                blueRedGreen.setImage(filterImage.toBRG());
                blackAndWhite.setImage(filterImage.toBlackAndWhite());
                sepia.setImage(filterImage.toSepia());
                prewitt.setImage(filterImage.toPrewitt());
            }
        }
    }

    @FXML
    public void showImage() {
        addTag.setText(null);
        rotation(0);
        getCurrentImage();
        imageView.setImage(filterImage.getImage());
        blueRedGreen.setImage(filterImage.toBRG());
        blackAndWhite.setImage(filterImage.toBlackAndWhite());
        sepia.setImage(filterImage.toSepia());
        prewitt.setImage(filterImage.toPrewitt());
    }

    @FXML
    public void verticalInvert() {
        getCurrentImage();
        imageView.setImage(filterImage.getImage());
        rotation(rotate + 180);
    }

    @FXML
    public void horizontalInvert() {
        getCurrentImage();
        imageView.setImage(filterImage.getImage());
        imageView.setRotate(0);
    }

    @FXML
    public void blueRedGreenFilter() {
        getCurrentImage();
        imageView.setImage(filterImage.toBRG());
    }

    @FXML
    public void blackAndWhiteFilter() {
        getCurrentImage();
        imageView.setImage(filterImage.toBlackAndWhite());
    }

    @FXML
    public void sepiaFilter() {
        getCurrentImage();
        imageView.setImage(filterImage.toSepia());
    }

    @FXML
    public void prewittFilter() {
        getCurrentImage();
        imageView.setImage(filterImage.toPrewitt());
    }

    public void getCurrentImage() {
        imageTags.getItems().clear();
        String file = items.getSelectionModel().getSelectedItem();
        for (RegisteredImages image : images) {
            if (image.getName().equals(file)) {
                filterImage = image;
                imageTagList.removeAll();
                imageTagList.addAll(filterImage.getTags());
                imageTags.setItems(imageTagList);
                break;
            }
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void loadImages() {
        File directory = Paths.get("src/main/resources/images/").toFile();
        for (File file : directory.listFiles()) {
            RegisteredImages img = new RegisteredImages(new Image(file.toURI().toString()), file);
            images.add(img);
            liste.add(img.getName());
        }
        items.setItems(liste);
    }

    public void imagesWithTags() {
        items.getItems().clear();
        System.out.println(images.size());
        /*for (RegisteredImages image : images) {
            if (!(image.getTags().containsAll(tagList))) {
                images.remove(image);
                liste.remove(image.getName());
            }
        }*/
        for (int i = 0; i < images.size(); i++) {

            if (!(images.get(i).getTags().stream().allMatch(tagList::contains))) {
                liste.remove(images.get(i).getName());
                images.remove(i);
            }
        }
        items.setItems(liste);
    }

    public void rotation(int x) {
        rotate = x;
        imageView.setRotate(rotate);
    }

}