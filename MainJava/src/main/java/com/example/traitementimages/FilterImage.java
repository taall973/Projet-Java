package com.example.traitementimages;

import javafx.scene.image.*;
import javafx.scene.paint.Color;

import java.io.File;

public class FilterImage {
    private Image image;
    private int[] inputPixels, outputPixels;
    private int height, width;
    private WritableImage writableImage;
    private PixelWriter pixelWriter;
    private PixelReader pixelReader;
    private int red, blue, green, opacity;

    public FilterImage(Image image) {
        this.image = image;
        width = (int) image.getWidth();
        height = (int) image.getHeight();
        inputPixels = new int[width * height * 4];
        pixelReader = image.getPixelReader();
        pixelReader.getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), inputPixels, 0, width * 4);
        writableImage = new WritableImage(pixelReader, width, height);
    }

    public void changeRGB(int p) {
        this.opacity = ((p >> 24) & 0xff);
        this.red = ((p >> 16) & 0xff);
        this.green = ((p >> 8) & 0xff);
        this.blue = (p & 0xff);
    }

    public Image toBRG() {
        pixelReader = image.getPixelReader();
        pixelReader.getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), inputPixels, 0, width * 4);
        writableImage = new WritableImage(pixelReader, width, height);
        pixelWriter = writableImage.getPixelWriter();
        outputPixels = new int[width * height * 4];
        int i = 0;
        int brg = 0;
        for (int pixel : inputPixels) {
            changeRGB(pixel);
            brg = ((this.opacity << 24) + (this.blue << 16) + (this.red << 8) + this.green);
            outputPixels[i++] = brg;
        }
        pixelWriter.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), outputPixels, 0, width * 4);
        return writableImage;
    }

    public Image toBlackAndWhite() {
        pixelReader = image.getPixelReader();
        pixelReader.getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), inputPixels, 0, width * 4);
        writableImage = new WritableImage(pixelReader, width, height);
        pixelWriter = writableImage.getPixelWriter();
        outputPixels = new int[width * height * 4];
        int i = 0;
        int sum = 0;
        for (int pixel : inputPixels) {
            changeRGB(pixel);
            sum = ((opacity << 24) + (((red + green + blue) / 3) << 16) + (((red + green + blue) / 3) << 8) + ((red + green + blue) / 3));
            outputPixels[i++] = sum;
        }
        pixelWriter.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), outputPixels, 0, width * 4);
        return writableImage;
    }

    public Image toSepia() {
        pixelReader = image.getPixelReader();
        pixelReader.getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), inputPixels, 0, width * 4);
        writableImage = new WritableImage(pixelReader, width, height);
        pixelWriter = writableImage.getPixelWriter();
        outputPixels = new int[width * height * 4];
        int i = 0;
        int sepia = 0;
        int redSepia = 0;
        int greenSepia = 0;
        int blueSepia = 0;
        for (int pixel : inputPixels) {
            changeRGB(pixel);
            redSepia = (int) ((red * .393) + (green * .769) + (blue * .189));
            if (redSepia > 255) {
                redSepia = 255;
            }
            greenSepia = (int) ((red * .349) + (green * .686) + (blue * .168));
            if (greenSepia > 255) {
                greenSepia = 255;
            }
            blueSepia = (int) ((red * .272) + (green * .534) + (blue * .131));
            if (blueSepia > 255) {
                blueSepia = 255;
            }
            sepia = (opacity << 24) + (redSepia << 16) + (greenSepia << 8) + blueSepia;
            outputPixels[i++] = sepia;
        }
        pixelWriter.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), outputPixels, 0, width * 4);
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