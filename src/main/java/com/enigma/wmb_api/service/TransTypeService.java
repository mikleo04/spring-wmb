package com.enigma.wmb_api.service;

import com.enigma.wmb_api.constant.TransactionType;
import com.enigma.wmb_api.dto.response.TransTypeResponse;

import java.util.List;

public interface TransTypeService {
    TransTypeResponse getById(TransactionType id);
    List<TransTypeResponse> getAll();
}
