package com.kgm.nextnest.repository;

import com.kgm.nextnest.model.RecentlyViewed;
import com.kgm.nextnest.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecentlyViewedRepository extends JpaRepository<RecentlyViewed, Long> {

    Optional<RecentlyViewed> findByCustomerIdAndApartmentId(
            Long customerId,
            Long apartmentId
    );

    List<RecentlyViewed> findTop10ByCustomerOrderByViewedAtDesc(User customer);

    List<RecentlyViewed> findByCustomerOrderByViewedAtDesc(User customer);
}