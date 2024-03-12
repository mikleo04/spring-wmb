package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.SearchTransactionRequest;
import com.enigma.wmb_api.dto.request.TransactionRequest;
import com.enigma.wmb_api.dto.request.UpdateStatusTransactionRequest;
import com.enigma.wmb_api.dto.response.ReportResponse;
import com.enigma.wmb_api.dto.response.TransactionResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface TransactionService {
    TransactionResponse create(TransactionRequest request);
    Page<TransactionResponse> getAll(SearchTransactionRequest request);
    List<ReportResponse> getReportCsv(SearchTransactionRequest request);
    void getReportPdf(HttpServletResponse response, SearchTransactionRequest request) throws IOException;
    TransactionResponse getById(String id);
    void updateStatus(UpdateStatusTransactionRequest request);
}
