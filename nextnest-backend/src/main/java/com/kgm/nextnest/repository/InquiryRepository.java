package com.kgm.nextnest.repository;

import com.kgm.nextnest.model.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    List<Inquiry> findByCustomer_Id(Long customerId);

    List<Inquiry> findByApartment_Owner_Id(Long ownerId);

    @Query("""
SELECT i
FROM Inquiry i
WHERE i.apartment.owner.id = :ownerId
""")
    List<Inquiry>
    findByApartmentOwnerId(
            @Param("ownerId")
            Long ownerId
    );

    long count();
}