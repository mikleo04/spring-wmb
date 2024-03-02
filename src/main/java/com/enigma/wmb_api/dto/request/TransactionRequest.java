package com.enigma.wmb_api.dto.request;

import com.enigma.wmb_api.constant.TransactionType;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TransactionRequest {

    private String customerId;

    private String tableId;

    private TransactionType transTypeid;

    private List<TransactionDetailRequest> detailRequests;

}
