package com.enigma.wmb_api.dto.request;

import jakarta.validation.constraints.Min;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TransactionDetailRequest {

    private String menuId;

    @Min(value = 1, message = "quantity must be greater than or equal 1")
    private Integer quantity;

}
