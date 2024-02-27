package com.enigma.wmb_api.service;

import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService{

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
        return null;
    }

    @Override
    public Menu update(Menu menu) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public void updateStatus(String id) {

    }
}
