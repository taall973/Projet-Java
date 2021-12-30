package com.example.traitementimages;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.File;

public class FilterImage {
    private Image image;


    public FilterImage(Image image) {
        this.image = image;
    }

    public Image toBRG() {
        PixelReader pixelReader = image.getPixelReader();
        int height = (int) image.getHeight();
        int width = (int) image.getWidth();
        WritableImage writableImage = new WritableImage(pixelReader, width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = pixelReader.getArgb(i, j);
                int opacity = ((pixel >> 24) & 0xff);
                int red = ((pixel >> 16) & 0xff);
                int green = ((pixel >> 8) & 0xff);
                int blue = ((pixel & 0xff));

                int brg = ((opacity << 24) + (blue << 16) + (red << 8) + green);
                pixelWriter.setArgb(i, j, brg);
            }
        }
        return writableImage;
    }

    public Image toBlackAndWhite() {
        PixelReader pixelReader = image.getPixelReader();
        int height = (int) image.getHeight();
        int width = (int) image.getWidth();
        WritableImage writableImage = new WritableImage(pixelReader, width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = pixelReader.getArgb(i, j);
                int opacity = ((pixel >> 24) & 0xff);
                int red = ((pixel >> 16) & 0xff);
                int green = ((pixel >> 8) & 0xff);
                int blue = ((pixel & 0xff));

                int sum = ((opacity << 24) + (((red + green + blue) / 3) << 16) + (((red + green + blue) / 3) << 8) + ((red + green + blue) / 3));
                pixelWriter.setArgb(i, j, sum);
            }
        }
        return writableImage;
    }

    public Image toSepia() {
        PixelReader pixelReader = image.getPixelReader();
        int height = (int) image.getHeight();
        int width = (int) image.getWidth();
        WritableImage writableImage = new WritableImage(pixelReader, width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = pixelReader.getArgb(i, j);
                int opacity = ((pixel >> 24) & 0xff);
                int red = ((pixel >> 16) & 0xff);
                int green = ((pixel >> 8) & 0xff);
                int blue = ((pixel & 0xff));

                int redSepia = (int) ((red * .393) + (green * .769) + (blue * .189));
                if (redSepia > 255) {
                    redSepia = 255;
                }
                int greenSepia = (int) ((red * .349) + (green * .686) + (blue * .168));
                if (greenSepia > 255) {
                    greenSepia = 255;
                }
                int blueSepia = (int) ((red * .272) + (green * .534) + (blue * .131));
                if (blueSepia > 255) {
                    blueSepia = 255;
                }
                int sepia = (opacity << 24) + (redSepia << 16) + (greenSepia << 8) + blueSepia;
                pixelWriter.setArgb(i, j, sepia);
            }
        }
        return writableImage;
    }

    public Image toPrewitt() {
        Image blackAndWhite = toBlackAndWhite();
        PixelReader pixelReader = blackAndWhite.getPixelReader();
        int height = (int) blackAndWhite.getHeight();
        int width = (int) blackAndWhite.getWidth();
        WritableImage writableImage = new WritableImage(pixelReader, width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int intensity[][] = new int[3][3]; //ce tableau contient les valeurs des pixels de l'image nécessaires à la convolution
                intensity[1][1] = pixelReader.getArgb(i, j) & 0xff;
                if (i == 0) {
                    intensity[0][0] = 0;
                    intensity[0][1] = 0;
                    intensity[0][2] = 0;
                    intensity[2][1] = pixelReader.getArgb(i + 1, j) & 0xff;
                    if (j == 0) {
                        intensity[1][0] = 0;
                        intensity[1][2] = pixelReader.getArgb(i, j + 1) & 0xff;
                        intensity[2][0] = 0;
                        intensity[2][2] = pixelReader.getArgb(i + 1, j + 1) & 0xff;
                    } else if (j == height - 1) {
                        intensity[1][0] = pixelReader.getArgb(i, j - 1) & 0xff;
                        intensity[1][2] = 0;
                        intensity[2][0] = pixelReader.getArgb(i + 1, j - 1) & 0xff;
                        intensity[2][2] = 0;
                    } else {
                        intensity[1][0] = pixelReader.getArgb(i, j - 1) & 0xff;
                        intensity[1][1] = pixelReader.getArgb(i, j) & 0xff;
                        intensity[1][2] = pixelReader.getArgb(i, j + 1) & 0xff;
                        intensity[2][0] = pixelReader.getArgb(i + 1, j - 1) & 0xff;
                        intensity[2][2] = pixelReader.getArgb(i + 1, j + 1) & 0xff;
                    }
                } else if (i == width - 1) {
                    intensity[2][0] = 0;
                    intensity[2][1] = 0;
                    intensity[2][2] = 0;
                    intensity[0][1] = pixelReader.getArgb(i - 1, j) & 0xff;
                    if (j == 0) {
                        intensity[0][0] = 0;
                        intensity[0][2] = pixelReader.getArgb(i - 1, j + 1) & 0xff;
                        intensity[1][0] = 0;
                        intensity[1][2] = pixelReader.getArgb(i, j + 1) & 0xff;
                    } else if (j == height - 1) {
                        intensity[0][0] = pixelReader.getArgb(i - 1, j - 1) & 0xff;
                        intensity[0][2] = 0;
                        intensity[1][0] = pixelReader.getArgb(i, j - 1) & 0xff;
                        intensity[1][2] = 0;
                    } else {
                        intensity[0][0] = pixelReader.getArgb(i - 1, j - 1) & 0xff;
                        intensity[0][2] = pixelReader.getArgb(i - 1, j + 1) & 0xff;
                        intensity[1][0] = pixelReader.getArgb(i, j - 1) & 0xff;
                        intensity[1][2] = pixelReader.getArgb(i, j + 1) & 0xff;
                    }
                } else {
                    intensity[0][1] = pixelReader.getArgb(i - 1, j) & 0xff;
                    intensity[2][1] = pixelReader.getArgb(i + 1, j) & 0xff;
                    if (j == 0) {
                        intensity[0][0] = 0;
                        intensity[0][2] = pixelReader.getArgb(i - 1, j + 1) & 0xff;
                        intensity[1][0] = 0;
                        intensity[1][2] = pixelReader.getArgb(i, j + 1) & 0xff;
                        intensity[2][0] = 0;
                        intensity[2][2] = pixelReader.getArgb(i + 1, j + 1) & 0xff;
                    } else if (j == height - 1) {
                        intensity[0][0] = pixelReader.getArgb(i - 1, j - 1) & 0xff;
                        intensity[0][2] = 0;
                        intensity[1][0] = pixelReader.getArgb(i, j - 1) & 0xff;
                        intensity[1][2] = 0;
                        intensity[2][0] = pixelReader.getArgb(i + 1, j - 1) & 0xff;
                        intensity[2][2] = 0;
                    } else {
                        intensity[0][0] = pixelReader.getArgb(i - 1, j - 1) & 0xff;
                        intensity[0][2] = pixelReader.getArgb(i - 1, j + 1) & 0xff;
                        intensity[1][0] = pixelReader.getArgb(i, j - 1) & 0xff;
                        intensity[1][2] = pixelReader.getArgb(i, j + 1) & 0xff;
                        intensity[2][0] = pixelReader.getArgb(i + 1, j - 1) & 0xff;
                        intensity[2][2] = pixelReader.getArgb(i + 1, j + 1) & 0xff;
                    }
                }
                int opacity = ((intensity[1][1] >> 24) & 0xff);

                int gX = intensity[0][0] * 1 + intensity[0][1] * 0 + intensity[0][2] * -1 +
                        intensity[1][0] * 1 + intensity[1][1] * 0 + intensity[1][2] * -1 +
                        intensity[2][0] * 1 + intensity[2][1] * 0 + intensity[2][2] * -1;
                int gY = intensity[0][0] * 1 + intensity[0][1] * 1 + intensity[0][2] * 1 +
                        intensity[1][0] * 0 + intensity[1][1] * 0 + intensity[1][2] * 0 +
                        intensity[2][0] * -1 + intensity[2][1] * -1 + intensity[2][2] * -1;
                int g = (int) Math.sqrt(Math.pow(gX, 2) + Math.pow(gY, 2));
                if (g > 255) {
                    g = 255;
                }
                int prewitt = (255 << 24) + (g << 16) + (g << 8) + g;
                pixelWriter.setArgb(i, j, prewitt);
            }
        }
        return writableImage;
    }

}