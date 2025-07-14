package com.free.map.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class DeliveryZoneRequest {
    private Long storeId;
    private String zoneName;
    private String borough;
    private BigDecimal minOrderAmount;
    private BigDecimal baseDeliveryFee;
    private BigDecimal perMileFee;
    private BigDecimal surgeMultiplier;
    private Integer estimatedDeliveryTime;
    private List<CoordinateDTO> coordinates;
}
