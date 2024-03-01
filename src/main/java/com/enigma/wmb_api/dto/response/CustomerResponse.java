package com.enigma.wmb_api.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomerResponse {
    private String id;

    private String name;

    private String mobilePhoneNumber;

    private Boolean isMember;
}
