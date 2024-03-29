package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.ResponseMessage;
import com.enigma.wmb_api.constant.TransactionStatus;
import com.enigma.wmb_api.constant.UrlApi;
import com.enigma.wmb_api.dto.request.SearchTransactionRequest;
import com.enigma.wmb_api.dto.request.TransactionRequest;
import com.enigma.wmb_api.dto.request.UpdateStatusTransactionRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.PagingResponse;
import com.enigma.wmb_api.dto.response.ReportResponse;
import com.enigma.wmb_api.dto.response.TransactionResponse;
import com.enigma.wmb_api.service.TransactionService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Tag(name = "Transaction", description = "The Transaction API. Contains all the operations that can be performed on a transaction.")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = UrlApi.TRANSACTION_API)
public class TransactionController {

    private final TransactionService service;

    @Operation(summary = "Create transaction")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'CUSTOMER')")
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<TransactionResponse>> createTransaction(@RequestBody TransactionRequest request) {
        TransactionResponse transactionResult = service.create(request);

        CommonResponse<TransactionResponse> response = CommonResponse.<TransactionResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(transactionResult)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all transaction")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<List<TransactionResponse>>> getAllTransaction(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sortBy", defaultValue = "transDate") String sortBy,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction,
            @RequestParam(name = "date", required = false) @JsonFormat(pattern = "yyyy-MM-dd") String date,
            @RequestParam(name = "startDate", required = false) @JsonFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam(name = "endDate", required = false) @JsonFormat(pattern = "yyyy-MM-dd") String endDate,
            @RequestParam(name = "customerId", required = false) String customerId,
            @RequestParam(name = "transTypeId", required = false) String transTypeId,
            @RequestParam(name = "transStatus", required = false) String transStatus
    ) {
        SearchTransactionRequest request = SearchTransactionRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .date(date)
                .startDate(startDate)
                .endDate(endDate)
                .customerId(customerId)
                .transactionTypeId(transTypeId)
                .transactionStatus(transStatus)
                .build();

        Page<TransactionResponse> transactionsResult = service.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPage(transactionsResult.getTotalPages())
                .totalElement(transactionsResult.getTotalElements())
                .page(transactionsResult.getPageable().getPageNumber() + 1)
                .size(transactionsResult.getSize())
                .hasNext(transactionsResult.hasNext())
                .hasPrevious(transactionsResult.hasPrevious())
                .build();

        CommonResponse<List<TransactionResponse>> response = CommonResponse.<List<TransactionResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(transactionsResult.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get transaction by id")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping(
            path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<TransactionResponse>> getTransactionById(@PathVariable String id) {
        TransactionResponse transactionResult = service.getById(id);

        CommonResponse<TransactionResponse> response = CommonResponse.<TransactionResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(transactionResult)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update status transaction after payment")
    @PostMapping("/status")
    public ResponseEntity<CommonResponse<?>> updateStatusTransaction(
            @RequestBody Map<String, Object> request
    ) {
        UpdateStatusTransactionRequest statusTransactionRequest = UpdateStatusTransactionRequest.builder()
                .orderId(request.get("order_id").toString())
                .transactionStatus(TransactionStatus.valueOf(request.get("transaction_status").toString().toUpperCase()))
                .build();

        service.updateStatus(statusTransactionRequest);

        return ResponseEntity.ok(CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .build());

    }

    @Operation(summary = "Download report transaction")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping(path = "/report-csv")
    public void donwloadReportTransactionCsv(
            HttpServletResponse response,
            @RequestParam(name = "date", required = false) @JsonFormat(pattern = "yyyy-MM-dd") String date,
            @RequestParam(name = "startDate", required = false) @JsonFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam(name = "endDate", required = false) @JsonFormat(pattern = "yyyy-MM-dd") String endDate,
            @RequestParam(name = "transTypeId", required = false) String transTypeId,
            @RequestParam(name = "transStatus", required = false) String transStatus
    ) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {

        SearchTransactionRequest request = SearchTransactionRequest.builder()
                .date(date)
                .startDate(startDate)
                .endDate(endDate)
                .transactionTypeId(transTypeId)
                .transactionStatus(transStatus)
                .build();

        List<ReportResponse> reportCsv = service.getReportCsv(request);
        reportCsv.add(0, ReportResponse.builder()
                .id("ID")
                .customer("Customer")
                .transType("Transaction Type")
                .menu("Menu")
                .price("Price")
                .table("Table")
                .transactionStatus("Transaction Status")
                .transDate("Transaction Date")
                .build());

        String fileName = "Report_transaction.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\""+fileName+"\"");

        StatefulBeanToCsv<ReportResponse> writer = new StatefulBeanToCsvBuilder<ReportResponse>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        writer.write(reportCsv);
    }

    @GetMapping("/report-pdf")
    public void donwloadReportTransactionPdf(
            HttpServletResponse response,
            @RequestParam(name = "date", required = false) @JsonFormat(pattern = "yyyy-MM-dd") String date,
            @RequestParam(name = "startDate", required = false) @JsonFormat(pattern = "yyyy-MM-dd") String startDate,
            @RequestParam(name = "endDate", required = false) @JsonFormat(pattern = "yyyy-MM-dd") String endDate,
            @RequestParam(name = "transTypeId", required = false) String transTypeId,
            @RequestParam(name = "transStatus", required = false) String transStatus
    ) throws IOException {

        SearchTransactionRequest request = SearchTransactionRequest.builder()
                .date(date)
                .startDate(startDate)
                .endDate(endDate)
                .transactionTypeId(transTypeId)
                .transactionStatus(transStatus)
                .build();

        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        service.getReportPdf(response, request);
    }

}
