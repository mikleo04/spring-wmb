package com.enigma.wmb_api.service;

import com.enigma.wmb_api.entity.Image;
import com.enigma.wmb_api.entity.Menu;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    List<Image> create(Menu menu, List<MultipartFile> multipartFiles);
    Resource getById(String id);
    void deleteByID(String id);

}
