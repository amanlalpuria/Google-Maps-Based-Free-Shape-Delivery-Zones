package com.free.map.service;

import com.free.map.dto.DeliveryInfoDTO;

import java.math.BigDecimal;

public interface StoreLocatorService {
    DeliveryInfoDTO getDeliveryInfo(BigDecimal customerLat, BigDecimal customerLng);
}
