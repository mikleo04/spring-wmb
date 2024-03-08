package com.enigma.wmb_api.entity;

import com.enigma.wmb_api.constant.TableName;
import com.enigma.wmb_api.constant.UserRole;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = TableName.ROLE_TABLE)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

}
