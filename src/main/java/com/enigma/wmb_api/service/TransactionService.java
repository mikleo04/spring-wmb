package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.SearchTransactionRequest;
import com.enigma.wmb_api.dto.request.TransactionRequest;
import com.enigma.wmb_api.dto.request.UpdateStatusTransactionRequest;
import com.enigma.wmb_api.dto.response.ReportCsvResponse;
import com.enigma.wmb_api.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TransactionService {
    TransactionResponse create(TransactionRequest request);
    Page<TransactionResponse> getAll(SearchTransactionRequest request);
    List<ReportCsvResponse> getReportCsv(SearchTransactionRequest request);
    TransactionResponse getById(String id);
    void updateStatus(UpdateStatusTransactionRequest request);
}
