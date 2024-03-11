package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.entity.Image;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.repository.ImageRepository;
import com.enigma.wmb_api.service.ImageService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final Path directoryPath;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository, @Value("${wmb.multipart.path_location}") String directoryPath) {
        this.imageRepository = imageRepository;
        this.directoryPath = Paths.get(directoryPath);
    }

    @PostConstruct
    public void initDirectory() {
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectory(directoryPath);
            } catch (IOException exception) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
            }
        }
    }

    @Override
    public List<Image> create(Menu menu, List<MultipartFile> multipartFiles) {

        List<Image> images = multipartFiles.stream().map(multipartFile -> {
            try {

                if (!List.of("image/jpeg", "image/png", "image/jpg", "image/svg+xml").contains(multipartFile.getContentType())) {
                    throw new ConstraintViolationException("Invalid Content Type", null);
                }

                String fileName = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();
                Path filePath = directoryPath.resolve(fileName);
                Files.copy(multipartFile.getInputStream(), filePath);

                Image image = Image.builder()
                        .name(fileName)
                        .path(filePath.toString())
                        .contentType(multipartFile.getContentType())
                        .size(multipartFile.getSize())
                        .menu(menu)
                        .build();
                return imageRepository.saveAndFlush(image);

            } catch (IOException exception) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
            }
        }).toList();

        return images;
    }

    @Override
    public Resource getById(String id) {
        try {
            Image image = imageRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "image not fount"));
            Path filePath = Paths.get(image.getPath());
            if (!Files.exists(filePath)) throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "INternal server error");

            return new UrlResource(filePath.toUri());
        } catch (IOException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        try {
            Image image = imageRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "image not found"));

            Path filePath = Paths.get(image.getPath());
            if (!Files.exists(filePath)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "image not found");

            imageRepository.deleteById(id);
            Files.delete(filePath);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }
}
