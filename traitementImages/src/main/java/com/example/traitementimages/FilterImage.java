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
       // this.writableImage = new WritableImage(this.pixelReader , this.width , this.height) ;
    }

//    public void firstFilter (){
//        int pix =0 ;
//        for ( int i=0 ; i < this.width ; i++) {
//            for (int j = 0; j < this.height; j++) {
//                pix = this.pixelReader.getArgb(i , j) ;
//                System.out.println(pix);
//            }
//        }
//    }


}
