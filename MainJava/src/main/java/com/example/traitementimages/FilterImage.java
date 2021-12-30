package com.example.traitementimages;

import javafx.scene.image.*;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    public Image getImage() {
        return this.image;
    }

    public Image bottomToTop() {
        pixelReader = image.getPixelReader();
        pixelReader.getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), inputPixels, 0, width * 4);
        writableImage = new WritableImage(pixelReader, width, height);
        pixelWriter = writableImage.getPixelWriter();
        outputPixels = new int[width * height * 4];
        /*int j = 0;
        for (int i = inputPixels.length - 1; i > 0; i--) {
            outputPixels[j++] = inputPixels[i];
        }*/
        pixelWriter.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), inputPixels, height * width * 4 - 1, width * 4);
        return writableImage;
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


    public int getPixelIntensity(int i) {
        try {
            return inputPixels[i] & 0xff;
        } catch (Exception e) {
            return 0;
        }
    }

    public Image toPrewitt() {
        pixelReader = image.getPixelReader();
        pixelReader.getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), inputPixels, 0, width * 4);
        writableImage = new WritableImage(pixelReader, width, height);
        pixelWriter = writableImage.getPixelWriter();
        outputPixels = new int[width * height * 4];
        int i = 0;
        int prewitt, gX, gY, g;
        int[][] intensity = new int[3][3];
        for (int pixel : inputPixels) {
            intensity[0][0] = getPixelIntensity(i - 1 - (width * 4));
            intensity[0][1] = getPixelIntensity(i - (width * 4));
            intensity[0][2] = getPixelIntensity(i + 1 - (width * 4));
            intensity[1][0] = getPixelIntensity(i - 1);
            intensity[1][1] = getPixelIntensity(i);
            intensity[1][2] = getPixelIntensity(i + 1);
            intensity[2][0] = getPixelIntensity(i - 1 + (width * 4));
            intensity[2][1] = getPixelIntensity(i + (width * 4));
            intensity[2][2] = getPixelIntensity(i + 1 + (width * 4));

            gX = intensity[0][0] + intensity[1][0] + intensity[2][0] - intensity[0][2] - intensity[1][2] - intensity[2][2];
            gY = intensity[0][0] + intensity[0][1] + intensity[0][2] - intensity[2][0] - intensity[2][1] - intensity[2][2];
            g = (int) Math.sqrt(Math.pow(gX, 2) + Math.pow(gY, 2));
            if (g > 255) {
                g = 255;
            }
            prewitt = (255 << 24) + (g << 16) + (g << 8) + g;

            outputPixels[i++] = prewitt;
        }
        pixelWriter.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), outputPixels, 0, width * 4);
        return writableImage;
    }
}