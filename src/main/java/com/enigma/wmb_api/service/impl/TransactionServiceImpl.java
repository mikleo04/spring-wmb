package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.SearchTransactionRequest;
import com.enigma.wmb_api.dto.request.TransactionRequest;
import com.enigma.wmb_api.dto.response.CustomerResponse;
import com.enigma.wmb_api.dto.response.TransTypeResponse;
import com.enigma.wmb_api.dto.response.TransactionDetailResponse;
import com.enigma.wmb_api.dto.response.TransactionResponse;
import com.enigma.wmb_api.entity.*;
import com.enigma.wmb_api.repository.TransactionRepository;
import com.enigma.wmb_api.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;
    private final CustomerService customerService;
    private final TableService tableService;
    private final TransTypeService transTypeService;
    private final TransactionDetailService transactionDetailService;
    private final MenuService menuService;

    @Transactional
    @Override
    public TransactionResponse create(TransactionRequest request) {

        CustomerResponse customerResponse = customerService.getById(request.getCustomerId());
        Customer customer = Customer.builder()
                .id(customerResponse.getId())
                .name(customerResponse.getName())
                .mobilePhoneNumber(customerResponse.getMobilePhoneNumber())
                .isMember(customerResponse.getIsMember())
                .build();

        // table validation
        DiningTable table = null;
        if (request.getTableId()  != null) {
            table = tableService.getById(request.getTableId());
        }

        // save transaction
        TransTypeResponse transTypeResponse = transTypeService.getById(request.getTransTypeid());
        TransType transType = TransType.builder()
                .id(transTypeResponse.getId())
                .description(transTypeResponse.getDescription())
                .build();

        Transaction trx = Transaction.builder()
                .customer(customer)
                .transDate(new Date())
                .table(table)
                .transType(transType)
                .build();
        repository.saveAndFlush(trx);

        //save transactionDetail
        List<TransactionDetail> trxDetails = request.getDetailRequests().stream()
                .map(detailRequest -> {

                    Menu menu = menuService.getById(detailRequest.getMenuId());

                    return TransactionDetail.builder()
                            .transaction(trx)
                            .menu(menu)
                            .menuPrice(menu.getPrice())
                            .quantity(detailRequest.getQuantity())
                            .build();
                }).toList();

        transactionDetailService.createBulk(trxDetails);
        trx.setTransactionDetails(trxDetails);

        List<TransactionDetailResponse> trxDetailResponse =trxDetails.stream()
                .map(trxDetail -> {
                    return TransactionDetailResponse.builder()
                            .id(trxDetail.getId())
                            .menuId(trxDetail.getMenu().getId())
                            .menuPrice(trxDetail.getMenuPrice())
                            .quantity(trxDetail.getQuantity())
                            .build();
                }).toList();

        // table validation
        String tableResponse = null;
        if (trx.getTable() != null) {
            tableResponse = trx.getTable().getId();
        }

        return TransactionResponse.builder()
                .id(trx.getId())
                .customerId(trx.getCustomer().getId())
                .tableId(tableResponse)
                .transTypeId(trx.getTransType().getId())
                .detailTransaction(trxDetailResponse)
                .transDate(trx.getTransDate())
                .build();
    }

    @Override
    public Page<TransactionResponse> getAll(SearchTransactionRequest request) {

        if (request.getPage() <= 0) request.setPage(1);

        Sort sorting = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());

        Pageable pageable = PageRequest.of(request.getPage()-1, request.getSize(), sorting);

        Page<Transaction> transactions = repository.findAll(pageable);

        return transactions.map(transaction -> {
            List<TransactionDetailResponse> transactionDetailResponses = transaction.getTransactionDetails().stream()
                    .map(transDetail -> {
                        return TransactionDetailResponse.builder()
                                .id(transDetail.getId())
                                .menuId(transDetail.getMenu().getId())
                                .menuPrice(transDetail.getMenuPrice())
                                .quantity(transDetail.getQuantity())
                                .build();
                    }).toList();

            String tableId = null;
            if(transaction.getTable() != null) {
                tableId = transaction.getTable().getId();
            }

            return TransactionResponse.builder()
                    .id(transaction.getId())
                    .transTypeId(transaction.getTransType().getId())
                    .customerId(transaction.getCustomer().getId())
                    .transDate(transaction.getTransDate())
                    .tableId(tableId)
                    .detailTransaction(transactionDetailResponses)
                    .build();
        });
    }
}
