package com.example.traitementimages;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Test {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        SecureRandom hasher = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[20];
        hasher.nextBytes(salt);
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        SecureRandom shuffler = SecureRandom.getInstance("SHA1PRNG");
        shuffler.setSeed(messageDigest.digest("password".getBytes(StandardCharsets.UTF_8)));
        int[] outputPixels;
        int[] cryptedPos = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] inputPixels = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        outputPixels = new int[10];
        for (int i = outputPixels.length - 1; i > 0; i--) {
            int j = shuffler.nextInt(i + 1);
            int temp = inputPixels[i];
            int temp2 = cryptedPos[i];
            cryptedPos[i] = cryptedPos[j];
            cryptedPos[j] = temp2;
            inputPixels[i] = inputPixels[j];
            inputPixels[j] = temp;
        }
        System.out.println("CRYPTED");
        for (int i = 0; i < 10; i++) {
            System.out.println(inputPixels[i] + " " + cryptedPos[i] + " " + inputPixels[cryptedPos[i]]);
        }
    }
}
