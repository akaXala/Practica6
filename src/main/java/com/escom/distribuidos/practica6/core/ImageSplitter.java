package com.escom.distribuidos.practica6.core;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Divide la imagen en fragmentos o bloques de filas (chunkHeight)
 * para repartir el trabajo equitativamente entre los hilos trabajadores.
 */
public class ImageSplitter {
    public List<ImageTask> createTask(BufferedImage image, int numThreads, CountDownLatch latch) {
        List<ImageTask> tasks = new ArrayList<>();
        int totalHeight = image.getHeight();
        int chunkHeight = totalHeight / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int startY = i * chunkHeight;
            int endY = (i == numThreads - 1) ? totalHeight : (startY + chunkHeight);

            ImageTask task = new ImageTask(i + 1, image, startY, endY, latch);
            tasks.add(task);
        }

        return tasks;
    }
}
