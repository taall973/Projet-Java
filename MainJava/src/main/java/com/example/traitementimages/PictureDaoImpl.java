package com.example.traitementimages;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import javafx.collections.ObservableList;

import java.util.ArrayList;

@XmlRootElement

@XmlAccessorType(XmlAccessType.FIELD)
public class PictureDaoImpl implements PictureDao {
    @XmlElement
    private ArrayList<Picture> pictures;
    @XmlElement
    private ObservableList<String> picturesOrder;

    public PictureDaoImpl() {
        this.pictures = new ArrayList<>();
    }

    public void addPicture(Picture picture) {
        pictures.add(picture);
    }

    @Override
    public ArrayList<Picture> getPictures() {
        return this.pictures;
    }

    @Override
    public void setPictures(ArrayList<Picture> pictures) {
        this.pictures = pictures;
    }

    @Override
    public ObservableList<String> getPicturesOrder() {
        return this.picturesOrder;
    }

    @Override
    public void setPicturesOrder(ObservableList<String> picturesOrder) {
        this.picturesOrder = picturesOrder;
    }

    @Override
    public Picture filter(Picture picture, int i, boolean onload) {
        System.out.println(picture.getFile().getName() + " " + picture.getId());
        Picture current = pictures.get(picture.getId());
        if (!onload) {
            current.addChange(i);
        }
        switch (i) {
            case 1:
                current.setFilteredImage(current.toBRG());
                break;
            case 2:
                current.setFilteredImage(current.toBlackAndWhite());
                break;
            case 3:
                current.setFilteredImage(current.toSepia());
                break;
            case 4:
                current.setFilteredImage(current.toPrewitt());
                break;
            default:
                current.setFilteredImage(current.getImage());
                current.setChanges(new ArrayList<>());
        }
        return current;
    }

}
