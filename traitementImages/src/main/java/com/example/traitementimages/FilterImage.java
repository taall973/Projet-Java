package com.example.traitementimages;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.File;

public class FilterImage {
    private Image image;
    private PixelReader pixelReader;
    private PixelWriter pixelWriter;
    private int width, height;
    private WritableImage writableImage;


    public FilterImage(Image image) {
        this.image = image;
        this.pixelReader = image.getPixelReader();
        this.height = (int) image.getHeight();
        this.width = (int) image.getWidth();
        this.writableImage = new WritableImage(this.pixelReader, this.width, this.height);
        pixelWriter = writableImage.getPixelWriter();
    }

    public Image firstFilter() {
        int pix = 0;
        File file = new File("./test.jpg");

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                int pixel = pixelReader.getArgb(i, j);

                pixelWriter.setArgb(i, j, pixel >> 1);
            }
        }
    /*
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                Color pixel = pixelReader.getColor(i, j);
                int b = Color.BLUE ;
                new Color(R,G,B);
                color.getR
                                  R     V           B
                    new COLOR (0>255 , 0>255 ,  , 0>255 )
                    (150 , 20,255)
                    150+20+255/3 = 120
                     pixel (i ; j ) (120 ,120 ,120)
                pixelWriter.setColor(i, j, Color.GREEN);
            }
        }
*/
        return this.writableImage;
    }
}