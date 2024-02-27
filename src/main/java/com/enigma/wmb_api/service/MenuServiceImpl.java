package com.enigma.wmb_api.service;

import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return null;
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
