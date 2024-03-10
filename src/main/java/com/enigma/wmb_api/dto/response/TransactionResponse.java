package com.enigma.wmb_api.dto.response;

import com.enigma.wmb_api.constant.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TransactionResponse {

    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date transDate;

    private String customerId;

    private String tableId;

    private TransactionType transTypeId;

    private List<TransactionDetailResponse> detailTransaction;

    private PaymentResponse payment;

}
