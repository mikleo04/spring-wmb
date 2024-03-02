package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.MenuRequest;
import com.enigma.wmb_api.dto.request.SearchMenuRequest;
import com.enigma.wmb_api.dto.response.MenuResponse;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.repository.MenuRepository;
import com.enigma.wmb_api.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository repository;

    @Override
    public MenuResponse create(MenuRequest request) {
        Menu menu = Menu.builder()
                .name(request.getName())
                .price(request.getPrice())
                .status(request.getStatus())
                .build();

        Menu menuResponse = repository.saveAndFlush(menu);

        return MenuResponse.builder()
                .id(menuResponse.getId())
                .name(menuResponse.getName())
                .price(menuResponse.getPrice())
                .status(menuResponse.getStatus())
                .build();
    }

    @Override
    public MenuResponse getById(String id) {
        Optional<Menu> menu = repository.findById(id);
        if (menu.isEmpty()) throw new RuntimeException("Menu not found " + HttpStatus.NOT_FOUND);
        return MenuResponse.builder()
                .id(menu.get().getId())
                .name(menu.get().getName())
                .price(menu.get().getPrice())
                .status(menu.get().getStatus())
                .build();
    }

    @Override
    public Page<MenuResponse> getAll(SearchMenuRequest request) {

        if (request.getPage() <= 0) request.setPage(1);

        Sort sorting = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());

        Pageable pageable = PageRequest.of(request.getPage()-1, request.getSize(), sorting);
        Page<Menu> menuResponses = repository.findAll(pageable);

        return menuResponses.map(menu -> {
            return MenuResponse.builder()
                    .id(menu.getId())
                    .name(menu.getName())
                    .price(menu.getPrice())
                    .status(menu.getStatus())
                    .build();
        });

    }

    @Override
    public MenuResponse update(MenuRequest request) {
        getById(request.getId());

        Menu menu = Menu.builder()
                .id(request.getId())
                .name(request.getName())
                .price(request.getPrice())
                .status(request.getStatus())
                .build();

        Menu menuResponse = repository.saveAndFlush(menu);

        return MenuResponse.builder()
                .id(menuResponse.getId())
                .name(menuResponse.getName())
                .price(menuResponse.getPrice())
                .status(menuResponse.getStatus())
                .build();
    }

    @Override
    public void delete(String id) {
        getById(id);
        repository.deleteById(id);
    }

    @Override
    public void updateStatus(String id, Boolean status) {
        getById(id);
        repository.updateStatus(id, status);
    }
}
