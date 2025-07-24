package com.free.map.serviceImpl;

import com.free.map.dto.CoordinateDTO;
import com.free.map.dto.DeliveryZoneRequest;
import com.free.map.entity.DeliveryZone;
import com.free.map.entity.DeliveryZoneCoordinate;
import com.free.map.entity.Store;
import com.free.map.repository.DeliveryZoneCoordinateRepository;
import com.free.map.repository.DeliveryZoneRepository;
import com.free.map.repository.StoreRepository;
import com.free.map.service.DeliveryZoneService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class DeliveryZoneServiceImpl implements DeliveryZoneService {

    @Autowired
    private DeliveryZoneRepository zoneRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private DeliveryZoneCoordinateRepository coordinateRepository;


    @Override
    @Transactional
    public DeliveryZone createZone(DeliveryZoneRequest request) {
        Store store = new Store();
        store.setStoreName(request.getStoreName());
        store.setStoreAddress(request.getStoreAddress());
        store.setStorePhone(request.getStorePhone());
        Store storeSaved = storeRepository.save(store);

        DeliveryZone zone = new DeliveryZone();
        zone.setZoneName(request.getZoneName());
        zone.setStoreId(storeSaved.getStoreId());
        zone.setBorough(request.getBorough());
        zone.setMinOrderAmount(request.getMinOrderAmount());
        zone.setBaseDeliveryFee(request.getBaseDeliveryFee());
        zone.setPerMileFee(request.getPerMileFee());
        zone.setFreeDeliveryRadius(request.getFreeDeliveryRadius());
        zone.setSurgeMultiplier(request.getSurgeMultiplier());
        zone.setEstimatedDeliveryTime(request.getEstimatedDeliveryTime());
        zone.setIsActive(true);

        DeliveryZone saved = zoneRepository.save(zone);

        List<CoordinateDTO> coords = request.getCoordinates();
        IntStream.range(0, coords.size()).forEach(i -> {
            CoordinateDTO dto = coords.get(i);
            DeliveryZoneCoordinate coord = new DeliveryZoneCoordinate();
            coord.setZone(saved);
            coord.setLatitude(dto.getLatitude());
            coord.setLongitude(dto.getLongitude());
            coord.setPointOrder(i);
            coordinateRepository.save(coord);
        });

        List<DeliveryZoneCoordinate> savedCoords = coordinateRepository.findByZoneId(saved.getId());
        saved.setCoordinates(savedCoords);

        return saved;
    }

    @Override
    public List<DeliveryZone> getZonesByStore(Long storeId) {
        return zoneRepository.findByStoreId(storeId);
    }

    @Override
    public DeliveryZone getZoneById(Long zoneId) {
        return zoneRepository.findById(zoneId).orElseThrow();
    }
}
