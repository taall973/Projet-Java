package com.example.traitementimages;

import java.util.ArrayList;

public interface PictureDao {
    public ArrayList<Picture> getPictures();
    public void setPictures(ArrayList<Picture> pictures);
    public Picture filter(Picture picture, int i, boolean onload);
}
