package com.free.map.serviceImpl;

import com.free.map.dto.DeliveryInfoDTO;
import com.free.map.entity.DeliveryZone;
import com.free.map.entity.DeliveryZoneCoordinate;
import com.free.map.repository.DeliveryZoneRepository;
import com.free.map.repository.StoreRepository;
import com.free.map.service.StoreLocatorService;
import com.free.map.utility.GeoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreLocatorServiceImpl implements StoreLocatorService {

    private final DeliveryZoneRepository deliveryZoneRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Override
    public DeliveryInfoDTO getDeliveryInfo(BigDecimal customerLat, BigDecimal customerLng) {
        List<DeliveryZone> allZones = deliveryZoneRepository.findAllWithCoordinates();

        DeliveryZone nearestZone = null;
        double minDistance = Double.MAX_VALUE;
        boolean insidePolygon = false;

        for (DeliveryZone zone : allZones) {
            List<DeliveryZoneCoordinate> coordinates = zone.getCoordinates();

            if (coordinates.isEmpty()) continue;

            boolean isInside = isPointInsidePolygon(customerLat, customerLng, coordinates);

            // Calculate centroid for distance measurement
            BigDecimal centroidLat = coordinates.stream()
                    .map(DeliveryZoneCoordinate::getLatitude)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(coordinates.size()), 8, RoundingMode.HALF_UP);

            BigDecimal centroidLng = coordinates.stream()
                    .map(DeliveryZoneCoordinate::getLongitude)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(coordinates.size()), 8, RoundingMode.HALF_UP);

            double distance = GeoUtils.calculateDistanceMiles(customerLat, customerLng, centroidLat, centroidLng);

            if (distance < minDistance) {
                nearestZone = zone;
                minDistance = distance;
                insidePolygon = isInside;
            }
        }

        if (nearestZone == null) {
            return null;
        }

        BigDecimal totalFee = nearestZone.getBaseDeliveryFee();
        if (!insidePolygon && nearestZone.getPerMileFee() != null) {
            BigDecimal freeMiles = nearestZone.getFreeDeliveryRadius() != null ?
                    nearestZone.getFreeDeliveryRadius() : BigDecimal.ZERO;

            BigDecimal distanceMiles = BigDecimal.valueOf(minDistance).setScale(2, RoundingMode.HALF_UP);
            BigDecimal chargeableMiles = distanceMiles.subtract(freeMiles).max(BigDecimal.ZERO);

            totalFee = totalFee.add(
                    nearestZone.getPerMileFee().multiply(chargeableMiles).setScale(2, RoundingMode.HALF_UP)
            );
        }
        return DeliveryInfoDTO.builder()
                .storeId(nearestZone.getStoreId())
                .storeName(storeRepository.findById(nearestZone.getStoreId()).get().getStoreName()) // Prefer actual name from DB
                .zoneId(nearestZone.getId())
                .zoneName(nearestZone.getZoneName())
                .baseFee(nearestZone.getBaseDeliveryFee())
                .perMileFee(nearestZone.getPerMileFee())
                .totalFee(totalFee)
                .withinZone(insidePolygon)
                .estimatedDeliveryTime(nearestZone.getEstimatedDeliveryTime())
                .build();
    }

    private boolean isPointInsidePolygon(BigDecimal lat, BigDecimal lng, List<DeliveryZoneCoordinate> polygon) {
        int intersectCount = 0;
        for (int i = 0; i < polygon.size(); i++) {
            DeliveryZoneCoordinate a = polygon.get(i);
            DeliveryZoneCoordinate b = polygon.get((i + 1) % polygon.size());

            BigDecimal ax = a.getLongitude();
            BigDecimal ay = a.getLatitude();
            BigDecimal bx = b.getLongitude();
            BigDecimal by = b.getLatitude();

            if (rayIntersectsSegment(lat, lng, ay, ax, by, bx)) {
                intersectCount++;
            }
        }
        return (intersectCount % 2 == 1);
    }

    private boolean rayIntersectsSegment(BigDecimal lat, BigDecimal lng,
                                         BigDecimal ay, BigDecimal ax,
                                         BigDecimal by, BigDecimal bx) {

        if (ay.compareTo(by) > 0) {
            BigDecimal tempY = ay; ay = by; by = tempY;
            BigDecimal tempX = ax; ax = bx; bx = tempX;
        }

        if (lat.compareTo(ay) < 0 || lat.compareTo(by) > 0) {
            return false;
        }

        BigDecimal maxX = ax.max(bx);
        if (lng.compareTo(maxX) > 0) {
            return false;
        }

        BigDecimal adjustedLat = lat;
        if (lat.compareTo(ay) == 0 || lat.compareTo(by) == 0) {
            adjustedLat = lat.add(new BigDecimal("0.00000001"));
        }

        if (ax.compareTo(bx) == 0) {
            return lng.compareTo(ax) <= 0;
        }

        double xIntersect = ax.doubleValue() +
                (adjustedLat.doubleValue() - ay.doubleValue()) *
                        (bx.doubleValue() - ax.doubleValue()) /
                        (by.doubleValue() - ay.doubleValue());

        return lng.doubleValue() <= xIntersect;
    }
}
