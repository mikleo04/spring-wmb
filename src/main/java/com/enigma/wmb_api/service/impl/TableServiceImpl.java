package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.SearchTableRequest;
import com.enigma.wmb_api.entity.DiningTable;
import com.enigma.wmb_api.repository.TableRepository;
import com.enigma.wmb_api.service.TableService;
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

    @Override
    public Page<DiningTable> getAll(SearchTableRequest request) {

        if (request.getPage() <= 0) request.setPage(1);

        Sort sorting = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());

        Pageable pageable = PageRequest.of(request.getPage()-1, request.getSize(), sorting);

        return repository.findAll(pageable);
    }
}
