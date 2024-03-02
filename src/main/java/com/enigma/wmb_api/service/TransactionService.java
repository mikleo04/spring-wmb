package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.SearchTransactionRequest;
import com.enigma.wmb_api.dto.request.TransactionRequest;
import com.enigma.wmb_api.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;

public interface TransactionService {
    TransactionResponse create(TransactionRequest request);
    Page<TransactionResponse> getAll(SearchTransactionRequest request);
    TransactionResponse getById(String id);
}
