package com.enigma.wmb_api.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class SearchUSerAccountResquest {
    private Integer page;

    private Integer size;

    private String sortBy;

    private String direction;
}
