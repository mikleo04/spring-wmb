package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.entity.DiningTable;
import com.enigma.wmb_api.repository.TableRepository;
import com.enigma.wmb_api.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {

    private final TableRepository repository;

    @Override
    public DiningTable creat(DiningTable diningTable) {
        return repository.saveAndFlush(diningTable);
    }
}
