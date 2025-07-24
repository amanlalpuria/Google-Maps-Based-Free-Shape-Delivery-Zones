package com.free.map.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class DeliveryZoneRequest {
    private String storeName;
    private String storeAddress;
    private String storePhone;
    private String zoneName;
    private String borough;
    private BigDecimal minOrderAmount;
    private BigDecimal baseDeliveryFee;
    private BigDecimal perMileFee;
    private BigDecimal surgeMultiplier;
    private BigDecimal freeDeliveryRadius;
    private Integer estimatedDeliveryTime;
    private List<CoordinateDTO> coordinates;
}
