package com.escom.distribuidos.practica6.controller;

import com.escom.distribuidos.practica6.service.ImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {
    public final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = "/grayscale", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> processImage(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] processedBytes = imageService.convertToGrayscale(file.getBytes());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(processedBytes);
    }
}
