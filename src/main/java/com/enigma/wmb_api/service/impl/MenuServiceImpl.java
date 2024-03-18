package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.constant.UrlApi;
import com.enigma.wmb_api.dto.request.MenuRequest;
import com.enigma.wmb_api.dto.request.SearchMenuRequest;
import com.enigma.wmb_api.dto.response.ImageResponse;
import com.enigma.wmb_api.dto.response.MenuResponse;
import com.enigma.wmb_api.entity.Image;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.entity.Transaction;
import com.enigma.wmb_api.repository.MenuRepository;
import com.enigma.wmb_api.service.ImageService;
import com.enigma.wmb_api.service.MenuService;
import com.enigma.wmb_api.specification.MenuSpecification;
import com.enigma.wmb_api.specification.TransactionSpecification;
import com.enigma.wmb_api.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuServiceImpl implements MenuService {

    private final MenuRepository repository;
    private final ValidationUtil validationUtil;
    private final ImageService imageService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MenuResponse create(MenuRequest request) {
        validationUtil.validate(request);

        Menu menu = Menu.builder()
                .name(request.getName())
                .price(request.getPrice())
                .status(request.getStatus())
                .build();
        if (request.getImages() != null) {
            List<Image> images = imageService.create(menu, request.getImages());
            menu.setImages(images);
        } else {
            repository.saveAndFlush(menu);
        }

        return convertMenuToMenuResponse(menu);
    }

    @Transactional(readOnly = true)
    @Override
    public Menu getById(String id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    @Override
    public MenuResponse getOneById(String id) {
        Optional<Menu> menu = repository.findById(id);
        if (menu.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND);

        return convertMenuToMenuResponse(menu.get());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MenuResponse> getAll(SearchMenuRequest request) {

        if (request.getPage() <= 0) request.setPage(1);

        Sort sorting = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage()-1, request.getSize(), sorting);

        Specification<Menu> specification = MenuSpecification.getSpecification(request);

        return repository.findAll(specification, pageable).map(this::convertMenuToMenuResponse);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MenuResponse update(MenuRequest request) {
        Menu menu = getById(request.getId());
        List<String> currentIdImages = menu.getImages().stream().map(image -> image.getId()).toList();

        menu.setId(request.getId());
        menu.setName(request.getName());
        menu.setStatus(request.getStatus());
        menu.setPrice(request.getPrice());
        if (request.getImages() != null) {
           currentIdImages.forEach(imageService::deleteById);
            List<Image> images = imageService.create(menu, request.getImages());
            menu.setImages(images);
        } else {
            repository.saveAndFlush(menu);
        }

        return convertMenuToMenuResponse(menu);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        Menu menu = getById(id);
        menu.getImages().forEach(image -> {
            imageService.deleteById(image.getId());
        });
        repository.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatus(String id, Boolean status) {
        getOneById(id);
        repository.updateStatus(id, status);
    }

    private MenuResponse convertMenuToMenuResponse(Menu menu) {
        List<ImageResponse> imageResponses = null;
        if (menu.getImages() != null) {
            imageResponses = menu.getImages().stream().map(image -> {
                return ImageResponse.builder()
                        .name(image.getName())
                        .url(UrlApi.DOWNLOAD_MENU_IMAGE_API + image.getId())
                        .build();
            }).toList();
        }

        return MenuResponse.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(menu.getPrice())
                .status(menu.getStatus())
                .images(imageResponses)
                .build();
    }

}
