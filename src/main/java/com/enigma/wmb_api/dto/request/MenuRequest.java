package com.enigma.wmb_api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class MenuRequest {

    private String id;

    @NotBlank
    private String name;

    @NotNull
    @Min(value = 0, message = "price must be greater than or equal 0")
    private Long price;

    private Boolean status;

}
