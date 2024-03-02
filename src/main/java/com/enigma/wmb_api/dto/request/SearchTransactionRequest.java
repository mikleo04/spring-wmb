package com.enigma.wmb_api.dto.request;

import com.enigma.wmb_api.constant.TransactionType;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SearchTransactionRequest {

    private Integer page;

    private Integer size;

    private String sortBy;

    private String direction;

    private Date transDate;

    private String customerId;

    private String tableId;

    private TransactionType transactionTypeId;

}
