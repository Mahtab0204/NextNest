package com.kgm.nextnest.repository;

import com.kgm.nextnest.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository
        extends JpaRepository<Location, Long> {
}