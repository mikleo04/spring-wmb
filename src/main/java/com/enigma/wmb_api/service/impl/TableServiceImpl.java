package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.entity.DiningTable;
import com.enigma.wmb_api.repository.TableRepository;
import com.enigma.wmb_api.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {

    private final TableRepository repository;

    @Override
    public DiningTable creat(DiningTable diningTable) {
        return repository.saveAndFlush(diningTable);
    }

    @Override
    public DiningTable getById(String id) {
        Optional<DiningTable> table = repository.findById(id);
        if (table.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Table not found");
        return table.get();
    }
}
