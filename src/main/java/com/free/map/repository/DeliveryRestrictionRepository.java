package com.free.map.repository;

import com.free.map.entity.DeliveryRestriction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRestrictionRepository extends JpaRepository<DeliveryRestriction, Long> {
    List<DeliveryRestriction> findByZoneId(Long zoneId);
}
