package com.enigma.wmb_api.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomerRequest {

    private String id;

    private String name;

    private String mobilePhoneNumber;

    private Boolean isMember;

}
