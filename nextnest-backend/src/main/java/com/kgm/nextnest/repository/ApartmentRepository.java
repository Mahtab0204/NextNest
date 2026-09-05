package com.kgm.nextnest.repository;

import com.kgm.nextnest.model.Apartment;
import com.kgm.nextnest.model.ApartmentApprovalStatus;
import com.kgm.nextnest.model.Purpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ApartmentRepository
        extends JpaRepository<Apartment, Long>,JpaSpecificationExecutor<Apartment> {

    List<Apartment> findByPurpose(Purpose purpose);

    List<Apartment> findByBedrooms(Integer bedrooms);

    List<Apartment> findByPriceBetween(
            Double minPrice,
            Double maxPrice
    );

    List<Apartment> findByOwner_Id(Long ownerId);

    long countByOwner_Id(Long ownerId);

    long countByOwner_IdAndApprovalStatus(
            Long ownerId,
            ApartmentApprovalStatus approvalStatus
    );

    List<Apartment>
    findByTitleContainingIgnoreCaseOrAddressContainingIgnoreCase(String title, String address);

    List<Apartment> findByApprovalStatus(ApartmentApprovalStatus status);

    long countByApprovalStatus(ApartmentApprovalStatus status);

    List<Apartment> findTop5ByApprovalStatusOrderByCreatedAtDesc(
            ApartmentApprovalStatus status
    );

    @Query("""
SELECT a
FROM Apartment a
WHERE

LOWER(a.title)
LIKE LOWER(CONCAT('%', :keyword, '%'))

OR

CAST(a.id AS string)
LIKE CONCAT('%', :keyword, '%')

OR

LOWER(a.owner.fullName)
LIKE LOWER(CONCAT('%', :keyword, '%'))

OR

LOWER(a.owner.email)
LIKE LOWER(CONCAT('%', :keyword, '%'))

OR

a.owner.phone
LIKE CONCAT('%', :keyword, '%')
""")
    List<Apartment> searchForAdmin(
            String keyword
    );

    List<Apartment>
    findByLocation_AreaAndPurposeAndApprovalStatus(
            String area,
            Purpose purpose,
            ApartmentApprovalStatus status
    );


    List<Apartment> findByFeaturedTrue();

    List<Apartment> findByFeaturedTrueOrderByCreatedAtDesc();

    long countByFeaturedTrue();

    List<Apartment> findByOwner_IdAndFeaturedTrue(
            Long ownerId
    );

    List<Apartment> findByFeaturedTrueAndFeaturedEndDateBefore(
            LocalDate date
    );

    @Query("""
SELECT a
FROM Apartment a
WHERE a.approvalStatus =
com.kgm.nextnest.model.ApartmentApprovalStatus.APPROVED
ORDER BY
CASE WHEN a.featured = true THEN 0 ELSE 1 END,
a.createdAt DESC
""")
    List<Apartment> findHomepageApartments();


    @Query("""
SELECT a
FROM Apartment a
WHERE
a.purpose = :purpose
AND
a.approvalStatus =
com.kgm.nextnest.model.ApartmentApprovalStatus.APPROVED
ORDER BY
CASE WHEN a.featured = true THEN 0 ELSE 1 END,
a.createdAt DESC
""")
    List<Apartment> findByPurposeFeaturedFirst(
            Purpose purpose
    );


    @Query("""
SELECT a
FROM Apartment a
WHERE

LOWER(a.title)
LIKE LOWER(CONCAT('%', :keyword, '%'))

OR

LOWER(a.address)
LIKE LOWER(CONCAT('%', :keyword, '%'))

ORDER BY
CASE WHEN a.featured = true THEN 0 ELSE 1 END,
a.createdAt DESC
""")
    List<Apartment> searchFeaturedFirst(
            String keyword
    );


    @Query("""
SELECT a
FROM Apartment a
WHERE

a.location.area = :area

AND

a.purpose = :purpose

AND

a.approvalStatus =
com.kgm.nextnest.model.ApartmentApprovalStatus.APPROVED

ORDER BY
CASE WHEN a.featured = true THEN 0 ELSE 1 END,
a.createdAt DESC
""")
    List<Apartment> findByAreaFeaturedFirst(
            String area,
            Purpose purpose
    );
}