package com.free.map.controller;

import com.free.map.dto.DeliveryZoneRequest;
import com.free.map.entity.DeliveryZone;
import com.free.map.service.DeliveryZoneService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery-zones")
public class DeliveryZoneController {

    @Autowired
    private DeliveryZoneService zoneService;

    @PostMapping("/create")
    public ResponseEntity<DeliveryZone> createZone(@RequestBody DeliveryZoneRequest request) {
        return ResponseEntity.ok(zoneService.createZone(request));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<DeliveryZone>> getZonesByStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(zoneService.getZonesByStore(storeId));
    }

    @GetMapping("/{zoneId}")
    public ResponseEntity<DeliveryZone> getZone(@PathVariable Long zoneId) {
        return ResponseEntity.ok(zoneService.getZoneById(zoneId));
    }
}
