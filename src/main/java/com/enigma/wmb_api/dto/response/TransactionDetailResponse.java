package com.enigma.wmb_api.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TransactionDetailResponse {

    private String id;

    private String menuId;

    private Long menuPrice;

    private Integer quantity;

}
