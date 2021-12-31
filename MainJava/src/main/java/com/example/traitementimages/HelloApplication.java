package com.example.traitementimages;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ((HelloController) fxmlLoader.getController()).setStage(stage);
        stage.setTitle("Photogénie");
        stage.getIcons().add(new Image(Paths.get("src/main/resources/images/chevre.jpg").toFile().toURI().toString()));
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}