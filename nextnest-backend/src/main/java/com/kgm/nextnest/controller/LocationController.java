package com.kgm.nextnest.controller;

import com.kgm.nextnest.model.Location;
import com.kgm.nextnest.service.LocationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
@Tag(
        name = "CRUD REST APIs for Location Resource"
)
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<List<Location>> getAllLocations() {

        return ResponseEntity.ok(locationService.findAllLocations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(
            @PathVariable Long id) {

        return ResponseEntity.ok(locationService.getLocationById(id));
    }

    @PostMapping
    public ResponseEntity<Location> createLocation(
            @RequestBody Location location) {

        return ResponseEntity.ok(locationService.createLocation(location));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocation(
            @PathVariable Long id,
            @RequestBody Location location) {

        return ResponseEntity.ok(locationService.updateLocation(id, location));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLocation(
            @PathVariable Long id) {

        locationService.deleteLocation(id);

        return ResponseEntity.ok("Location deleted successfully");}
}