package com.enigma.wmb_api.entity;

import com.enigma.wmb_api.constant.TableName;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = TableName.CUSTOMER_TABLE)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "mobile_phone_no")
    private String mobilePhoneNumber;

    @Column(name = "is_member")
    private Boolean isMember;

}
