package com.example.exercice2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;

public class HelloController {

    @FXML
    private FileChooser fc;
    private ObservableList<String> liste = FXCollections.observableArrayList("item1","item2","item3","...");
    @FXML
    private ListView<String> items;

    @Override
    public void initialize(){
        items.setItems(liste);
        System.out.println("Done");
    }


}