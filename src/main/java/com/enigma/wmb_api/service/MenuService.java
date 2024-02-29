package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.SearchMenuRequest;
import com.enigma.wmb_api.entity.Menu;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MenuService {
    Menu create(Menu menu);
    Menu getById(String id);
    Page<Menu> getAll(SearchMenuRequest request);
    Menu update(Menu menu);
    void delete(String id);
    void updateStatus(String id, Boolean status);

}
