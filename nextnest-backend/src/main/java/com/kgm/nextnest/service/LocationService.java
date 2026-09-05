package com.kgm.nextnest.service;

import com.kgm.nextnest.model.Location;

import java.util.List;

public interface LocationService {

    List<Location> findAllLocations();

    Location createLocation(Location location);

    Location updateLocation(Long id, Location location);

    void deleteLocation(Long id);

    Location getLocationById(Long id);
}
