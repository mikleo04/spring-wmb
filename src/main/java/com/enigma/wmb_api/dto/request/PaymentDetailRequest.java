package com.enigma.wmb_api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentDetailRequest {
    @JsonProperty("order_id")
    private  String orderId;

    @JsonProperty("gross_amount")
    private Long amount;
}
