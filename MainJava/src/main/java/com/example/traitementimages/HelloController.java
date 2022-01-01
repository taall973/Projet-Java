package com.example.traitementimages;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
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

    private Picture currentPicture;
    private PictureDaoImpl images;
    Stage stage;
    @FXML
    private FileChooser fc;
    private ObservableList<String> imagesList = FXCollections.observableArrayList();
    private ObservableList<String> tagList = FXCollections.observableArrayList();
    private ObservableList<String> imageTagList = FXCollections.observableArrayList();
    @FXML
    private ListView<String> items, itemsTag, imageTags;
    @FXML
    private TextField searchTag, addTag;
    @FXML
    private ImageView imageView, noFilter, blueRedGreen, blackAndWhite, sepia, prewitt;

    @FXML
    protected void initialize() {
        images = new PictureDaoImpl();
        loadImages();
        items.getSelectionModel().select("placeholder-image.png");
        showImage();
        fc = new FileChooser();
        searchTag.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    if (!(tagList.contains(searchTag.getText()))) {
                        tagList.add(searchTag.getText());
                        itemsTag.setItems(tagList);
                        imagesWithTags();
                        searchTag.setText(null);
                    }
                }
            }
        });
        addTag.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    if (!(imageTagList.contains(addTag.getText()))) {
                        images.getPictures().get(items.getSelectionModel().getSelectedIndex()).addTag(addTag.getText());
                        imageTagList.add(addTag.getText());
                        imageTags.setItems(imageTagList);
                        addTag.setText(null);
                    }
                }
            }
        });
    }

    @FXML
    protected void chooseFile() throws IOException {
        File file = fc.showOpenDialog(stage);
        if (file != null) {
            if (imagesList.contains(file.getName())) {
                Alert sameName = new Alert(Alert.AlertType.INFORMATION, "Un fichier enregistré à ce nom existe déjà !");
                sameName.setTitle("Doublon détecté");
                sameName.setHeaderText(null);
                sameName.show();
                items.getSelectionModel().select(file.getName());
                showImage();
            } else {
                imagesList.add(file.getName());
                items.setItems(imagesList);
                Path source = Paths.get(file.getAbsolutePath());
                Path dest = Paths.get("src/main/resources/images/" + file.getName());
                Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
                currentPicture = new Picture(new Image(dest.toFile().toURI().toURL().toString()), file, images.getPictures().size());
                images.addPicture(currentPicture);
                imageView.setImage(currentPicture.getImage());
                noFilter.setImage(currentPicture.getImage());
                blueRedGreen.setImage(currentPicture.toBRG());
                blackAndWhite.setImage(currentPicture.toBlackAndWhite());
                sepia.setImage(currentPicture.toSepia());
                prewitt.setImage(currentPicture.toPrewitt());
                items.getSelectionModel().select(currentPicture.getFile().getName());
                rotation(0);
            }
        }
    }

    @FXML
    public void showImage() {
        addTag.setText(null);
        getCurrentImage();
        imageView.setImage(currentPicture.getFilteredImage());
        noFilter.setImage(currentPicture.getImage());
        blueRedGreen.setImage(currentPicture.toBRG());
        blackAndWhite.setImage(currentPicture.toBlackAndWhite());
        sepia.setImage(currentPicture.toSepia());
        prewitt.setImage(currentPicture.toPrewitt());
        if (currentPicture.isInvert()) {
            imageView.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        } else {
            imageView.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        }
        rotation(currentPicture.getRotation());
    }

    @FXML
    public void verticalInvert() {
        rotation(currentPicture.getRotation() + 180);
    }

    @FXML
    public void horizontalInvert() {
        currentPicture.setInvert(!currentPicture.isInvert());
        if (currentPicture.isInvert()) {
            imageView.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        } else {
            imageView.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        }
    }

    @FXML
    public void rotateLeft() {
        rotation(currentPicture.getRotation() - 90);
    }

    @FXML
    public void rotateRight() {
        rotation(currentPicture.getRotation() + 90);
    }

    @FXML
    public void removeFilters() {
        currentPicture = images.filter(currentPicture, 0, false);
        imageView.setImage(currentPicture.getFilteredImage());
        noFilter.setImage(currentPicture.getImage());
        blueRedGreen.setImage(currentPicture.toBRG());
        blackAndWhite.setImage(currentPicture.toBlackAndWhite());
        sepia.setImage(currentPicture.toSepia());
        prewitt.setImage(currentPicture.toPrewitt());
    }

    @FXML
    public void blueRedGreenFilter() {
        currentPicture = images.filter(currentPicture, 1, false);
        imageView.setImage(currentPicture.getFilteredImage());
        noFilter.setImage(currentPicture.getImage());
        blueRedGreen.setImage(currentPicture.toBRG());
        blackAndWhite.setImage(currentPicture.toBlackAndWhite());
        sepia.setImage(currentPicture.toSepia());
        prewitt.setImage(currentPicture.toPrewitt());
    }

    @FXML
    public void blackAndWhiteFilter() {
        currentPicture = images.filter(currentPicture, 2, false);
        imageView.setImage(currentPicture.getFilteredImage());
        noFilter.setImage(currentPicture.getImage());
        blueRedGreen.setImage(currentPicture.toBRG());
        blackAndWhite.setImage(currentPicture.toBlackAndWhite());
        sepia.setImage(currentPicture.toSepia());
        prewitt.setImage(currentPicture.toPrewitt());
    }

    @FXML
    public void sepiaFilter() {
        currentPicture = images.filter(currentPicture, 3, false);
        imageView.setImage(currentPicture.getFilteredImage());
        noFilter.setImage(currentPicture.getImage());
        blueRedGreen.setImage(currentPicture.toBRG());
        blackAndWhite.setImage(currentPicture.toBlackAndWhite());
        sepia.setImage(currentPicture.toSepia());
        prewitt.setImage(currentPicture.toPrewitt());
    }

    @FXML
    public void prewittFilter() {
        currentPicture = images.filter(currentPicture, 4, false);
        imageView.setImage(currentPicture.getFilteredImage());
        noFilter.setImage(currentPicture.getImage());
        blueRedGreen.setImage(currentPicture.toBRG());
        blackAndWhite.setImage(currentPicture.toBlackAndWhite());
        sepia.setImage(currentPicture.toSepia());
        prewitt.setImage(currentPicture.toPrewitt());
    }

    public void getCurrentImage() {
        imageTags.getItems().clear();
        String file = items.getSelectionModel().getSelectedItem();
        for (Picture picture : images.getPictures()) {
            if (picture.getFile().getName().equals(file)) {
                currentPicture = picture;
                imageTagList.removeAll();
                imageTagList.addAll(currentPicture.getTags());
                imageTags.setItems(imageTagList);
                break;
            }
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        items.requestFocus();
    }

    public void loadImages() {
        File directory = Paths.get("src/main/resources/images/").toFile();
        for (File file : directory.listFiles()) {
            Picture img = new Picture(new Image(file.toURI().toString()), file, images.getPictures().size());
            images.addPicture(img);
            imagesList.add(img.getFile().getName());
        }
        items.setItems(imagesList);
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(PictureDaoImpl.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            PictureDaoImpl pictureDao = (PictureDaoImpl) jaxbUnmarshaller.unmarshal(Paths.get("src/main/resources/save/dataPictures.xml").toFile());
            for (Picture picture : images.getPictures()) {
                Picture pictureTemp = pictureDao.getPictures().get(picture.getId());
                picture.setFile(pictureTemp.getFile());
                picture.setTags(pictureTemp.getTags());
                picture.setChanges(pictureTemp.getChanges());
                for (int i = 0; i < picture.getChanges().size(); i++) {
                    images.filter(picture, picture.getChanges().get(i), true);
                }
                picture.setRotation(pictureTemp.getRotation());
                picture.setId(pictureTemp.getId());
                picture.setInvert(pictureTemp.isInvert());
            }

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void imagesWithTags() {
        ObservableList<String> matchingList = FXCollections.observableArrayList();

        for (Picture picture : images.getPictures()) {
            if (tagList.stream().allMatch(picture.getTags()::contains)) {
                matchingList.add(picture.getFile().getName());
            }
        }
        imagesList = matchingList;
        items.setItems(imagesList);
    }

    public void rotation(int x) {
        currentPicture.setRotation(x);
        imageView.setRotate(currentPicture.getRotation());
    }

    @FXML
    public void save() {
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(PictureDaoImpl.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            PictureDaoImpl pictureDao = new PictureDaoImpl();
            pictureDao.setPictures(images.getPictures());
            jaxbMarshaller.marshal(pictureDao, Paths.get("src/main/resources/save/dataPictures.xml").toFile());

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}