package com.enigma.wmb_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomerRequest {

    private String id;

    @NotBlank(message = "name is required")
    private String name;

    @Pattern(message = "number phone is invalid", regexp = "(?:\\+62)?0?8\\d{2}(\\d{8})")
    private String mobilePhoneNumber;

    private Boolean isMember;

}
