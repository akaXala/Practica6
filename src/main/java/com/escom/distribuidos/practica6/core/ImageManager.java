package com.escom.distribuidos.practica6.core;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class ImageManager {
    public BufferedImage loadImage(String imagePath) {
        try {
            return ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.err.println("Error al cargar imagen: " + e.getMessage());
            return null;
        }
    }

    public void saveImage(BufferedImage image, String outputPath, String format) {
        try {
            ImageIO.write(image, format, new File(outputPath));
            System.out.println("Imagen guardada en: " + outputPath);
        } catch (Exception e) {
            System.err.println("Error al guardar: " + e.getMessage());
        }
    }

    // NUEVO: Convierte BufferedImage a byte[] para enviar por red
    public byte[] imageToBytes(BufferedImage image, String format) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, format, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    // NUEVO: Convierte byte[] a BufferedImage al recibir de la red
    public BufferedImage bytesToImage(byte[] imageBytes) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
            return ImageIO.read(bais);
        } catch (IOException e) {
            return null;
        }
    }
}