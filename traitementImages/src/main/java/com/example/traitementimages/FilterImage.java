package com.example.traitementimages;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class FilterImage {
    private Image image ;
    private PixelReader pixelReader ;
    private PixelWriter pixelWriter ;
    private int width , height ;
    private WritableImage writableImage  ;


    public FilterImage(Image image) {
        this.image = image;
        this.pixelReader = image.getPixelReader() ;
        this.height = (int) image.getHeight() ;
        this.width = (int) image.getWidth() ;
        System.out.println(this.width + " " + this.height);
        this.writableImage = new WritableImage(this.pixelReader , this.width , this.height) ;
        pixelWriter= writableImage.getPixelWriter() ;
    }

    public Image firstFilter (){
        int pix =0 ;
        for ( int i=0 ; i < this.width ; i++) {
            for (int j = 0; j < this.height; j++) {
                int pixel = pixelReader.getArgb(i, j);

                int alpha = ((pixel >> 24) & 0xff);
                int red = ((pixel >> 16) & 0xff);
                int green = ((pixel >> 8) & 0xff);
                int blue = (pixel & 0xff);
                int gray = (alpha << 24) ;
                System.out.print(gray);
                pixelWriter.setArgb(i, j, gray);

            }
        }
        return writableImage ;
    }

}
