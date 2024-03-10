package com.enigma.wmb_api.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentItemDetailRequest {
    private String id;
    private String productId;
    private Long price;
    private Integer quantity;
    private String name;
}