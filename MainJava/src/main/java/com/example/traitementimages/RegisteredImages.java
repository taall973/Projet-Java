package com.example.traitementimages;

import javafx.scene.image.Image;

import java.io.File;
import java.util.ArrayList;

public class RegisteredImages extends FilterImage { //implements quand on aura transformé FilterImage en interface
    ArrayList<String> tags;
    File name;

    public RegisteredImages(Image image) {
        super(image);
        tags = new ArrayList<>();
    }

    public RegisteredImages(Image image, File name) {
        this(image);
        this.name = name;
    }

    public ArrayList<String> getTags() {
        return this.tags;
    }

    public String getName() {
        return this.name.getName();
    }

    public void addTag(String tag) {
        tags.add(tag);
    }


}
