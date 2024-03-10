package com.enigma.wmb_api.dto.request;

import com.enigma.wmb_api.constant.TransactionStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UpdateStatusTransactionRequest {

    private String orderId;

    private TransactionStatus transactionStatus;

}

