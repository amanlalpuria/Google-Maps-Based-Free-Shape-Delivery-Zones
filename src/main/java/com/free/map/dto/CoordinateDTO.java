package com.free.map.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CoordinateDTO {
    private BigDecimal latitude;
    private BigDecimal longitude;
}
