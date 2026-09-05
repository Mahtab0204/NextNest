package com.kgm.nextnest.service;

import com.kgm.nextnest.dto.ApartmentRequest;
import com.kgm.nextnest.dto.PromoteApartmentRequest;
import com.kgm.nextnest.model.ApartmentApprovalStatus;
import com.kgm.nextnest.model.Purpose;
import com.kgm.nextnest.response.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ApartmentService {

    ApartmentResponse createApartment(
            ApartmentRequest request,
            String ownerEmail
    );

    ApartmentResponse getApartmentById(Long id);

    List<ApartmentResponse> getAllApartments();

    ApartmentResponse updateApartment(
            Long id,
            ApartmentRequest request
    );

    void deleteApartment(Long id);

    List<ApartmentResponse> getOwnerApartments(Long ownerId);

    String uploadApartmentImage(Long apartmentId, MultipartFile image);

    List<ApartmentResponse> searchApartments(String keyword);

    List<ApartmentResponse> getApartmentsByPriceRange(Double minPrice, Double maxPrice);

    List<ApartmentResponse> getApartmentsByBedrooms(Integer bedrooms);

    List<ApartmentResponse> getApartmentsByPurpose(Purpose purpose);

    List<ApartmentResponse> filterApartments(
            Purpose purpose,
            Integer bedrooms,
            Double minPrice,
            Double maxPrice
    );

    Page<ApartmentResponse> getAllApartments(
            int page,
            int size,
            String sortBy,
            String sortDir
    );

    public List<ApartmentImageResponse> getApartmentImages(Long apartmentId);

    void deleteImage(Long imageId);

    void setCoverImage(Long imageId);

    List<ApartmentResponse> getAllApartmentsForAdmin();

    void deleteApartmentByAdmin(Long id);

    ApartmentResponse approveApartment(Long id);

    ApartmentResponse rejectApartment(
            Long id,
            String reason
    );

    AdminApartmentDetailsResponse getApartmentDetailsForAdmin(Long id);

    List<ApartmentResponse> getApartmentsByApprovalStatus(ApartmentApprovalStatus status);

    List<ApartmentResponse> getApartmentsByArea(String area, Purpose purpose);

    FeatureApartmentResponse featureApartment(
            PromoteApartmentRequest request,
            String ownerEmail
    );
    ApartmentResponse promoteApartment(
            PromoteApartmentRequest request,
            String ownerEmail
    );
    List<ApartmentResponse> getHomepageApartments();

}
