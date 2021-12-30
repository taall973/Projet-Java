package com.example.traitementimages;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class RegisteredImages extends FilterImage { //implements quand on aura transformé FilterImage en interface
    ArrayList<String> tags;
    public RegisteredImages(Image image){
        super(image);
    }
    //public void AjouterTags(String t){
        tags.add(t);
    }
}
