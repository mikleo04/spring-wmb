package com.enigma.wmb_api.entity;

import com.enigma.wmb_api.constant.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = TableName.TRANSACTION_TABLE)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "trans_date", updatable = false)
    private Date transDate;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private DiningTable table;

    @ManyToOne
    @JoinColumn(name = "trans_type")
    private TransType transType;

    @OneToMany(mappedBy = "transaction")
    @JsonManagedReference
    private List<TransactionDetail>transactionDetails;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
}
