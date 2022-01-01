package com.example.traitementimages;

import javafx.collections.ObservableList;

import java.util.ArrayList;

public interface PictureDao {
    public ArrayList<Picture> getPictures();
    public void setPictures(ArrayList<Picture> pictures);
    public ObservableList<String> getPicturesOrder();
    public void setPicturesOrder(ObservableList<String> orderList);
    public Picture filter(Picture picture, int i, boolean onload);
}
