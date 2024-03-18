package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.constant.UrlApi;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Image Menu", description = "The Image API. Contains operations that can be performed on a image menu.")
@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "Download image by id")
    @GetMapping(path = UrlApi.DOWNLOAD_MENU_IMAGE_API +"{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable(name = "imageId") String imageId) {
        Resource image = imageService.getById(imageId);
        String headerValue = String.format("attachment; filename=%s", image.getFilename());
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(image);
    }

    @Operation(summary = "Delete image by id")
    @DeleteMapping(path = UrlApi.DOWNLOAD_MENU_IMAGE_API + "{id}")
    public ResponseEntity<CommonResponse<?>> deleteImage(@PathVariable(name = "id") String id) {
        imageService.deleteById(id);
        CommonResponse<Object> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .build();
        return ResponseEntity.ok(response);
    }


}
