package com.enigma.wmb_api.entity;

import com.enigma.wmb_api.constant.TableName;
import com.enigma.wmb_api.constant.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = TableName.PAYMENT_TABLE)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "token")
    private String token;

    @Column(name = "redirect_url")
    private String redirectUrl;

    @Column(name = "transaction_status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @OneToOne(mappedBy = "payment")
    private Transaction transaction;

}
