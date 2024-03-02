package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.SearchTableRequest;
import com.enigma.wmb_api.dto.request.TableRequest;
import com.enigma.wmb_api.dto.response.TableResponse;
import com.enigma.wmb_api.entity.DiningTable;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TableService {
    TableResponse creat(TableRequest diningTable);
    TableResponse getById(String id);
    Page<TableResponse> getAll(SearchTableRequest request);
    void delete(String id);
}
