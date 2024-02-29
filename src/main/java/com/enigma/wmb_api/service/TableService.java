package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.SearchTableRequest;
import com.enigma.wmb_api.entity.DiningTable;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TableService {
    DiningTable creat(DiningTable diningTable);
    DiningTable getById(String id);
    Page<DiningTable> getAll(SearchTableRequest request);
    void delete(String id);
}
