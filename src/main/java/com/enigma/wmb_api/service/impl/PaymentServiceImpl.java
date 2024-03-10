package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.PaymentMethod;
import com.enigma.wmb_api.constant.TransactionStatus;
import com.enigma.wmb_api.dto.request.PaymentDetailRequest;
import com.enigma.wmb_api.dto.request.PaymentItemDetailRequest;
import com.enigma.wmb_api.dto.request.PaymentRequest;
import com.enigma.wmb_api.entity.Payment;
import com.enigma.wmb_api.entity.Transaction;
import com.enigma.wmb_api.repository.PaymentRepository;
import com.enigma.wmb_api.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service

public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RestClient restClient;
    private final String SECRET_KEY;
    private final String SNAP_URL;

    @Autowired
    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            RestClient restClient,
            @Value("${midtrans.api.key}") String secretKey,
            @Value("${midtrans.api.url}") String snapUrl
    ) {
        this.paymentRepository = paymentRepository;
        this.restClient = restClient;
        SECRET_KEY = secretKey;
        SNAP_URL = snapUrl;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Payment createPayment(Transaction transaction) {
        Long amount = transaction.getTransactionDetails().stream()
                .mapToLong(value -> value.getQuantity() * value.getMenuPrice())
                .reduce(0, Long::sum);

        List<PaymentItemDetailRequest>itemDetailRequestsList = transaction.getTransactionDetails().stream()
                .map(transactionDetail -> {
                    return PaymentItemDetailRequest.builder()
                            .name(transactionDetail.getMenu().getName())
                            .price(transactionDetail.getMenuPrice())
                            .quantity(transactionDetail.getQuantity())
                            .build();
                }).toList();

        PaymentRequest request = PaymentRequest.builder()
                .paymentDetail(PaymentDetailRequest.builder()
                        .orderId(transaction.getId())
                        .amount(amount)
                        .build())
                .paymentItemDetails(itemDetailRequestsList)
                .paymentMethod(List.of(PaymentMethod.SHOPEEPAY, PaymentMethod.GOPAY))
                .build();

        ResponseEntity<Map<String, String>> response = restClient.post()
                .uri(SNAP_URL)
                .body(request)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + SECRET_KEY)
                .retrieve().toEntity(new ParameterizedTypeReference<>() {});

        Map<String, String> body = response.getBody();

        Payment payment = Payment.builder()
                .token(body.get("token"))
                .redirectUrl(body.get("redirect_url"))
                .transactionStatus(TransactionStatus.ORDERED)
                .build();

        paymentRepository.saveAndFlush(payment);

        return payment;
    }

    @Override
    public void checkFailedAndUpdatePayment() {

    }
}
