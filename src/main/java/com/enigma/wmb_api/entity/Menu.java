package com.enigma.wmb_api.entity;

import com.enigma.wmb_api.constant.TableName;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Table(name = TableName.MENU_TABLE)
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false, columnDefinition = "BIGINT CHECK (price >= 0)")
    private Long price;

    @Column(name = "status", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean status;
}
