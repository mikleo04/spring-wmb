package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.TransactionDetailRequest;
import com.enigma.wmb_api.dto.response.TransactionResponse;
import com.enigma.wmb_api.entity.TransactionDetail;
import com.enigma.wmb_api.repository.TransactionDetailRepository;
import com.enigma.wmb_api.service.TransactionDetailService;
import com.enigma.wmb_api.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TransaactionDetailServiceImpl implements TransactionDetailService {

    private final TransactionDetailRepository repository;
    private final ValidationUtil validationUtil;

    @Override
    public List<TransactionDetail> createBulk(List<TransactionDetail> transactionDetails) {
        validationUtil.validate(transactionDetails);
        return repository.saveAllAndFlush(transactionDetails);
    }
}
