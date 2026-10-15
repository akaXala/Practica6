package com.escom.distribuidos.practica6.core;

import java.awt.image.BufferedImage;
import java.util.concurrent.CountDownLatch;

public class ImageTask implements Runnable {
    private final int taskId;
    private final BufferedImage image;
    private final int startY;
    private final int endY;
    private final CountDownLatch latch;

    public ImageTask(int taskId, BufferedImage image, int startY, int endY, CountDownLatch latch) {
        this.taskId = taskId;
        this.image = image;
        this.startY = startY;
        this.endY = endY;
        this.latch = latch;
    }

    public int getTaskId() {
        return taskId;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndY() {
        return endY;
    }

    @Override
    public void run() {
        System.out.println("[Hilo #" + taskId + " - " + Thread.currentThread().getName() + "] INICIÓ el procesamiento (Filas " + startY + " a " + (endY - 1) + ").");
        try {
            for (int y = startY; y < endY; y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int originalRgb = image.getRGB(x, y);
                    int newRgb = applyGrayScale(originalRgb);
                    image.setRGB(x, y, newRgb);
                }
            }
        } finally {
            // =========================================================================
            // MÉTODO DE SINCRONIZACIÓN: CountDownLatch
            // El hilo notifica que ha finalizado decrementando el contador atómico.
            // =========================================================================
            if (latch != null) {
                latch.countDown();
                System.out.println("[Hilo #" + taskId + " - " + Thread.currentThread().getName() + "] TERMINÓ su trabajo. [Sincronización: CountDownLatch decrementado -> Faltan por terminar: " + latch.getCount() + " hilo(s)]");
            } else {
                System.out.println("[Hilo #" + taskId + " - " + Thread.currentThread().getName() + "] TERMINÓ su trabajo.");
            }
        }
    }

    private int applyGrayScale(int rgb) {
        int alpha = (rgb >> 24) & 0xFF;

        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;

        int gray = (red + green + blue) / 3;

        return (alpha << 24) | (gray << 16) | (gray << 8) | gray;
    }
}
