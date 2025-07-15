package com.free.map.controller;

import com.free.map.dto.DeliveryInfoDTO;
import com.free.map.service.StoreLocatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerDeliveryController {

    private final StoreLocatorService storeLocatorService;

    @GetMapping("/get-delivery-info")
    public DeliveryInfoDTO getDeliveryInfo(
            @RequestParam("lat") BigDecimal lat,
            @RequestParam("lng") BigDecimal lng
    ) {
        return storeLocatorService.getDeliveryInfo(lat, lng);
    }
}
