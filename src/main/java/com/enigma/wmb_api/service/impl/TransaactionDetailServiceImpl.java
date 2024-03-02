package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.TransactionDetailRequest;
import com.enigma.wmb_api.dto.response.TransactionResponse;
import com.enigma.wmb_api.entity.TransactionDetail;
import com.enigma.wmb_api.repository.TransactionDetailRepository;
import com.enigma.wmb_api.service.TransactionDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TransaactionDetailServiceImpl implements TransactionDetailService {

    private final TransactionDetailRepository repository;

    @Override
    public List<TransactionDetail> createBulk(List<TransactionDetail> transactionDetails) {
        return repository.saveAllAndFlush(transactionDetails);
    }
}
