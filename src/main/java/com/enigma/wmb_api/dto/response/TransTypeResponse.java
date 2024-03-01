package com.enigma.wmb_api.dto.response;

import com.enigma.wmb_api.constant.TransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TransTypeResponse {

    @Enumerated(EnumType.STRING)
    private TransactionType id;

    private String description;

}
