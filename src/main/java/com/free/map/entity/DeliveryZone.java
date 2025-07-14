package com.free.map.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "delivery_zones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private String zoneName;

    @Column(nullable = false)
    private String borough;

    @Column(nullable = false)
    private BigDecimal minOrderAmount;

    @Column(nullable = false)
    private BigDecimal baseDeliveryFee;

    private BigDecimal perMileFee;

    private BigDecimal surgeMultiplier = BigDecimal.valueOf(1.0);

    private Integer estimatedDeliveryTime;

    private Boolean isActive = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DeliveryZoneCoordinate> coordinates;

    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeliveryRestriction> restrictions;
}
