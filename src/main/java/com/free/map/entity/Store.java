package com.free.map.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "store")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    @Column(name = "store_name", nullable = true)
    private String storeName;

    @Column(name = "store_address", nullable = true)
    private String storeAddress;

    @Column(name = "store_phone", nullable = true)
    private String storePhone;
}
