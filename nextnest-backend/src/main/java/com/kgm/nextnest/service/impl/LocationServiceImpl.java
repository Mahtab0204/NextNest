package com.kgm.nextnest.service.impl;

import com.kgm.nextnest.model.Location;
import com.kgm.nextnest.repository.LocationRepository;
import com.kgm.nextnest.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Override
    public List<Location> findAllLocations() {

        return locationRepository.findAll();
    }

    @Override
    public Location createLocation(Location location) {

        return locationRepository.save(location);
    }

    @Override
    public Location updateLocation(Long id, Location location) {

        Location existingLocation = locationRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Location not found"));

        existingLocation.setCity(location.getCity());

        existingLocation.setArea(location.getArea());

        existingLocation.setSubArea(location.getSubArea());

        return locationRepository.save(existingLocation);
    }

    @Override
    public void deleteLocation(Long id) {

        Location location = locationRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Location not found"));

        locationRepository.delete(location);
    }

    @Override
    public Location getLocationById(Long id) {

        return locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found"));
    }
}