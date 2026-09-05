package com.kgm.nextnest.repository;

import com.kgm.nextnest.model.FeaturedPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeaturedPaymentRepository extends JpaRepository<FeaturedPayment, Long> {

    List<FeaturedPayment>
    findByOwner_Id(Long ownerId);

    List<FeaturedPayment>
    findByApartment_Id(Long apartmentId);

    List<FeaturedPayment> findByOwner_IdOrderByPaidAtDesc(
            Long ownerId
    );
}