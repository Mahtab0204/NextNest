package com.kgm.nextnest.repository;

import com.kgm.nextnest.model.ApartmentImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApartmentImageRepository
        extends JpaRepository<ApartmentImage, Long> {

}