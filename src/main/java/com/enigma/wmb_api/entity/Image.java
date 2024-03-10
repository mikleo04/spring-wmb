package com.enigma.wmb_api.entity;

import com.enigma.wmb_api.constant.TableName;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = TableName.IMAGE_TABLE)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "size", nullable = false)
    private Long size;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

}
