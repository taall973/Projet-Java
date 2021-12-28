package com.example.exercice2;

import java.awt.Desktop;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class HelloController {

    private Desktop desktop = Desktop.getDesktop();

    @FXML
    BorderPane bp;
    @FXML
    Stage stage;
    @FXML
    private FileChooser fc;
    @FXML
    private Menu pickImage;
    private ObservableList<String> liste = FXCollections.observableArrayList("item1", "item2", "item3", "...");
    @FXML
    private ListView<File> items;
    @FXML
    private TextField text;

    @FXML
    protected void initialize() {
        //items.setItems(liste);
        stage = (Stage) bp.getScene().getWindow();
        System.out.println("Done");
    }

    @FXML
    protected void chooseFile() {
        pickImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                File file = fc.showOpenDialog(stage);
                if (file != null) {
                    openFile(file);
                    List<File> files = Arrays.asList(file);
                    ObservableList<File> listF = (ObservableList<File>) files;
                    items.setItems(listF);
                }
            }
        });
    }

    private void openFile(File file) {
        try {
            this.desktop.open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}