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

    private String date;

    private String startDate;

    private String endDate;

    private String customerId;

    private String transactionTypeId;

    private String transactionStatus;

}
