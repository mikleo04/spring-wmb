package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.SearchTableRequest;
import com.enigma.wmb_api.dto.request.TableRequest;
import com.enigma.wmb_api.dto.response.TableResponse;
import com.enigma.wmb_api.entity.DiningTable;
import com.enigma.wmb_api.repository.TableRepository;
import com.enigma.wmb_api.service.TableService;
import com.enigma.wmb_api.util.ValidationUtil;
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
    private final ValidationUtil validationUtil;

    @Override
    public TableResponse creat(TableRequest request) {
        validationUtil.validate(request);

        DiningTable table = DiningTable.builder()
                .name(request.getName())
                .build();

        DiningTable tableResponse = repository.saveAndFlush(table);

        return TableResponse.builder()
                .id(tableResponse.getId())
                .name(tableResponse.getName())
                .build();
    }

    @Override
    public TableResponse getById(String id) {
        Optional<DiningTable> table = repository.findById(id);
        if (table.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Table not found");
        DiningTable tableResponse = table.get();

        return TableResponse.builder()
                .id(tableResponse.getId())
                .name(tableResponse.getName())
                .build();

    }

    @Override
    public Page<TableResponse> getAll(SearchTableRequest request) {

        if (request.getPage() <= 0) request.setPage(1);

        Sort sorting = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());

        Pageable pageable = PageRequest.of(request.getPage()-1, request.getSize(), sorting);

        Page<DiningTable> tableResponses = repository.findAll(pageable);

        return tableResponses.map(table -> {
            return TableResponse.builder()
                    .id(table.getId())
                    .name(table.getName())
                    .build();
        });
    }

    @Override
    public void delete(String id) {
        getById(id);
        repository.deleteById(id);
    }
}
