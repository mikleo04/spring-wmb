package com.enigma.wmb_api.dto.response;

import com.opencsv.bean.CsvBindByPosition;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ReportResponse {

    @CsvBindByPosition(position = 1)
    private String id;

    @CsvBindByPosition(position = 2)
    private String transDate;

    @CsvBindByPosition(position = 3)
    private String customer;

    @CsvBindByPosition(position = 3)
    private String table;

    @CsvBindByPosition(position = 4)
    private String transType;

    @CsvBindByPosition(position = 5)
    private String menu;

    @CsvBindByPosition(position = 6)
    private String quantity;

    @CsvBindByPosition(position = 7)
    private String price;

    @CsvBindByPosition(position = 8)
    private String transactionStatus;

}
