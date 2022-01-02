package com.example.traitementimages;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.*;

import java.io.*;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;

@XmlRootElement

@XmlAccessorType(XmlAccessType.FIELD)
public class Picture implements Comparable {

    @XmlTransient
    private Image image, filteredImage;
    private File file;
    private ArrayList<String> tags;
    private ArrayList<Integer> changes;
    private byte[] password;
    @XmlTransient
    private int[] inputPixels, outputPixels;
    @XmlTransient
    private int width, height, red, green, blue, opacity;
    private int rotation, id;
    private boolean invert;
    @XmlTransient
    private WritableImage writableImage;
    @XmlTransient
    private PixelWriter pixelWriter;
    @XmlTransient
    private PixelReader pixelReader;

    private Picture() {
        tags = new ArrayList<>();
        changes = new ArrayList<>();
    }

    public Picture(Image image, File file, int id) {
        rotation = 0;
        invert = false;
        this.image = image;
        this.filteredImage = image;
        this.file = file;
        this.id = id;
        width = (int) image.getWidth();
        height = (int) image.getHeight();
        inputPixels = new int[width * height * 4];
        outputPixels = new int[width * height * 4];
        pixelReader = image.getPixelReader();
        pixelReader.getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), inputPixels, 0, width * 4);
        writableImage = new WritableImage(pixelReader, width, height);
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        MessageDigest mD = MessageDigest.getInstance("SHA-512");
        tags = new ArrayList<>();
        changes = new ArrayList<>();
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getFilteredImage() {
        return filteredImage;
    }

    public void setFilteredImage(Image image) {
        this.filteredImage = image;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<Integer> getChanges() {
        return changes;
    }

    public void setChanges(ArrayList<Integer> changes) {
        this.changes = changes;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public int[] getInputPixels() {
        return inputPixels;
    }

    public void setInputPixels(int[] inputPixels) {
        this.inputPixels = inputPixels;
    }

    public int[] getOutputPixels() {
        return outputPixels;
    }

    public void setOutputPixels(int[] outputPixels) {
        this.outputPixels = outputPixels;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getOpacity() {
        return opacity;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation % 360;
    }

    public boolean isInvert() {
        return invert;
    }

    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    public WritableImage getWritableImage() {
        return writableImage;
    }

    public void setWritableImage(WritableImage writableImage) {
        this.writableImage = writableImage;
    }

    public PixelWriter getPixelWriter() {
        return pixelWriter;
    }

    public void setPixelWriter(PixelWriter pixelWriter) {
        this.pixelWriter = pixelWriter;
    }

    public PixelReader getPixelReader() {
        return pixelReader;
    }

    public void setPixelReader(PixelReader pixelReader) {
        this.pixelReader = pixelReader;
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public void addChange(int i) {
        changes.add(i);
    }

    public void changeRGB(int p) {
        this.opacity = ((p >> 24) & 0xff);
        this.red = ((p >> 16) & 0xff);
        this.green = ((p >> 8) & 0xff);
        this.blue = (p & 0xff);
    }

    /* Image encryptImage() {
        pixelReader = filteredImage.getPixelReader();
        pixelReader.getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), inputPixels, 0, width * 4);
        writableImage = new WritableImage(pixelReader, width, height);
        pixelWriter = writableImage.getPixelWriter();
        outputPixels = new int[width * height * 4];
        int i = 0;
        int encrypt = 0;
        for (int pixel : inputPixels) {
            changeRGB(pixel);
            encrypt = ((Math.floorMod(this.opacity + 254, 255) << 24) + (Math.floorMod(this.red + 254, 255) << 16) + (Math.floorMod(this.green + 254, 255) << 8) + (Math.floorMod(this.blue + 254, 255)));
            outputPixels[i++] = encrypt;
        }
        pixelWriter.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), outputPixels, 0, width * 4);
        image = writableImage;
        filteredImage = writableImage;
        try {
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            for (int px : outputPixels) {
                out.write(px);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //On vide les attributs de l'image
        inputPixels = new int[width * height * 4];
        outputPixels = new int[width * height * 4];
        pixelReader = image.getPixelReader();
        pixelWriter = writableImage.getPixelWriter();
        //Et on enregistre le résultat dans l'image sauvegardée dans le dossier
        //BufferedWriter im = new BufferedWriter();
        return writableImage;
    }*/

    public Image encryptImage() {
        pixelReader = filteredImage.getPixelReader();
        pixelReader.getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), inputPixels, 0, width * 4);
        writableImage = new WritableImage(pixelReader, width, height);
        pixelWriter = writableImage.getPixelWriter();
        outputPixels = new int[width * height * 4];
        int i = 0;
        int encrypt = 0;
        for (int pixel : inputPixels) {

            outputPixels[i++] = pixel >> 24;
        }
        pixelWriter.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), outputPixels, 0, width * 4);
        /*try {
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            for (int px : outputPixels) {
                out.write(px);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //On vide les attributs de l'image
        image = writableImage;
        filteredImage = writableImage;
        inputPixels = new int[width * height * 4];
        outputPixels = new int[width * height * 4];
        pixelReader = image.getPixelReader();
        pixelWriter = writableImage.getPixelWriter();
        //Et on enregistre le résultat dans l'image sauvegardée dans le dossier
        //BufferedWriter im = new BufferedWriter();
        return writableImage;
    }

    public Image decryptImage() {
        pixelReader = filteredImage.getPixelReader();
        pixelReader.getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), inputPixels, 0, width * 4);
        writableImage = new WritableImage(pixelReader, width, height);
        pixelWriter = writableImage.getPixelWriter();
        outputPixels = new int[width * height * 4];
        int i = 0;
        int decrypt = 0;
        for (int pixel : inputPixels) {
            outputPixels[i++] = pixel << 24;
        }
        image = writableImage;
        filteredImage = writableImage;
        pixelWriter.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), outputPixels, 0, width * 4);
        return writableImage;
    }

    public Image toBRG() {
        pixelReader = filteredImage.getPixelReader();
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
        pixelReader = filteredImage.getPixelReader();
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
        pixelReader = filteredImage.getPixelReader();
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
        pixelReader = filteredImage.getPixelReader();
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

    @Override
    public int compareTo(Object o) {
        Picture picture = (Picture) o;
        if (this.file.getName().compareTo(picture.getFile().getName()) > 0) {
            return 1;
        } else if (this.file.getName().compareTo(picture.getFile().getName()) < 0) {
            return -1;
        } else {
            return 0;
        }

    }
}
