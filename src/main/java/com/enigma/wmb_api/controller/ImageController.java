package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.UrlApi;
import com.enigma.wmb_api.entity.Image;
import com.enigma.wmb_api.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping(path = UrlApi.DOWNLOAD_MENU_IMAGE_API +"{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable(name = "imageId") String imageId) {
        Resource image = imageService.getById(imageId);
        String headerValue = String.format("attachment; filename=%s", image.getFilename());
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(image);
    }


}
