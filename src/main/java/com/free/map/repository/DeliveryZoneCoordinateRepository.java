package com.free.map.repository;

import com.free.map.entity.DeliveryZoneCoordinate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryZoneCoordinateRepository extends JpaRepository<DeliveryZoneCoordinate, Long> {
    List<DeliveryZoneCoordinate> findByZone_IdOrderByPointOrder(Long zoneId);
    List<DeliveryZoneCoordinate> findByZoneId(Long zoneId);

}
