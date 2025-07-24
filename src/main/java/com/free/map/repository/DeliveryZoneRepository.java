package com.free.map.repository;

import com.free.map.entity.DeliveryZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryZoneRepository extends JpaRepository<DeliveryZone, Long> {
    List<DeliveryZone> findByStoreId(Long storeId);

    @Query("SELECT DISTINCT z FROM DeliveryZone z LEFT JOIN FETCH z.coordinates")
    List<DeliveryZone> findAllWithCoordinates();
}
