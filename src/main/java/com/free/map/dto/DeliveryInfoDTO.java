package com.free.map.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DeliveryInfoDTO {
    private Long storeId;
    private String storeName;
    private Long zoneId;
    private String zoneName;
    private BigDecimal baseFee;
    private BigDecimal perMileFee;
    private BigDecimal totalFee;
    private boolean withinZone;
    private int estimatedDeliveryTime;
}