package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.MenuRequest;
import com.enigma.wmb_api.dto.request.SearchMenuRequest;
import com.enigma.wmb_api.dto.response.MenuResponse;
import com.enigma.wmb_api.entity.Menu;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MenuService {
    MenuResponse create(MenuRequest request);
    MenuResponse getById(String id);
    Page<MenuResponse> getAll(SearchMenuRequest request);
    MenuResponse update(MenuRequest request);
    void delete(String id);
    void updateStatus(String id, Boolean status);

}
