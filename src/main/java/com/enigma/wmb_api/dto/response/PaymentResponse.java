package com.enigma.wmb_api.dto.response;

import com.enigma.wmb_api.constant.TransactionStatus;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentResponse {

    private String id;

    private String token;

    private String redirectUrl;

    private TransactionStatus transactionStatus;

}
