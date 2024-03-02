package com.enigma.wmb_api.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class MenuRequest {

    private String id;

    private String name;

    private Long price;

    private Boolean status;

}
