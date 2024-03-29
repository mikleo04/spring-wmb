package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.constant.TransactionType;
import com.enigma.wmb_api.dto.response.TransTypeResponse;
import com.enigma.wmb_api.entity.TransType;
import com.enigma.wmb_api.repository.TransTypeRepository;
import com.enigma.wmb_api.service.TransTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransTypeServiceImpl implements TransTypeService {
    private final TransTypeRepository repository;

    @Transactional(readOnly = true)
    @Override
    public TransTypeResponse getById(TransactionType id) {
        Optional<TransType> transType = repository.findById(id);

        if (transType.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND);

        return TransTypeResponse.builder()
                .id(transType.get().getId())
                .description(transType.get().getDescription())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<TransTypeResponse> getAll() {
        List<TransType> transTypes = repository.findAll();

        return transTypes.stream().map(transType -> {
           return TransTypeResponse.builder()
                   .id(transType.getId())
                   .description(transType.getDescription())
                   .build();
           }).toList();
    }
}
