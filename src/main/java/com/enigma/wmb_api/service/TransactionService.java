package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.TransactionRequest;
import com.enigma.wmb_api.dto.response.TransactionResponse;

public interface TransactionService {
    TransactionResponse create(TransactionRequest request);
}
