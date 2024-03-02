package com.enigma.wmb_api.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class MenuResponse {
    private String id;

    private String name;

    private Long price;

    private Boolean status;
}
