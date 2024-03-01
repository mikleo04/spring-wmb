package com.enigma.wmb_api.entity;

import com.enigma.wmb_api.constant.TableName;
import com.enigma.wmb_api.constant.TransactionType;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = TableName.TRANS_TYPE_TABLE)
public class TransType {

    @Id
    @Enumerated(EnumType.STRING)
    private TransactionType id;

    @Column(name = "description")
    private String description;

}
