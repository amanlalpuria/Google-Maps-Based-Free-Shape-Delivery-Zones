package com.free.map.service;

import com.free.map.dto.DeliveryZoneRequest;
import com.free.map.entity.DeliveryZone;

import java.util.List;

public interface DeliveryZoneService {
    DeliveryZone createZone(DeliveryZoneRequest request);
    List<DeliveryZone> getZonesByStore(Long storeId);
    DeliveryZone getZoneById(Long zoneId);
}
