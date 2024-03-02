package com.enigma.wmb_api.service.impl;

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
}
