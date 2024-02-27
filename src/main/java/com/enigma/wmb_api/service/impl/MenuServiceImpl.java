package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.repository.MenuRepository;
import com.enigma.wmb_api.service.MenuService;
import lombok.RequiredArgsConstructor;
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
    public Menu create(Menu menu) {
        return repository.saveAndFlush(menu);
    }

    @Override
    public Menu getById(String id) {
        Optional<Menu> menu = repository.findById(id);
        if (menu.isEmpty()) throw new RuntimeException("Menu not found " + HttpStatus.NOT_FOUND);
        return menu.get();
    }

    @Override
    public List<Menu> getAll() {
        return repository.findAll();
    }

    @Override
    public Menu update(Menu menu) {
        getById(menu.getId());
        return repository.saveAndFlush(menu);
    }

    @Override
    public void delete(String id) {
        Menu menuSelected = getById(id);
        repository.delete(menuSelected);
    }

    @Override
    public void updateStatus(String id, Boolean status) {
        getById(id);
        repository.updateStatus(id, status);
    }
}
