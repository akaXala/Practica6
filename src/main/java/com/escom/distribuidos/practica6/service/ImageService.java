package com.escom.distribuidos.practica6.service;

import com.escom.distribuidos.practica6.core.ImageManager;
import com.escom.distribuidos.practica6.core.ImageSplitter;
import com.escom.distribuidos.practica6.core.ImageTask;
import com.escom.distribuidos.practica6.core.ParallelProcessor;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service
public class ImageService {
    public byte[] convertToGrayscale(byte[] imageBytes) throws IOException {
        ImageManager manager = new ImageManager();
        BufferedImage image = manager.bytesToImage(imageBytes);

        int hilos = 4;
        CountDownLatch latch = new CountDownLatch(hilos);

        List<ImageTask> tasks = new ImageSplitter().createTask(image, hilos, latch);
        new ParallelProcessor().executeWithCountDownLatch(tasks, latch);

        return manager.imageToBytes(image, "png");
    }
}
