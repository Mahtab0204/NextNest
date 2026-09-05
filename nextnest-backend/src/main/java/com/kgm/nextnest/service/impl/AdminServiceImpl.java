package com.kgm.nextnest.service.impl;

import com.kgm.nextnest.model.*;
import com.kgm.nextnest.repository.ApartmentRepository;
import com.kgm.nextnest.repository.InquiryRepository;
import com.kgm.nextnest.repository.UserRepository;
import com.kgm.nextnest.response.*;
import com.kgm.nextnest.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    private final ApartmentRepository apartmentRepository;

    private final InquiryRepository inquiryRepository;


    @Override
    public AdminDashboardStatsResponse getDashboardStats() {
        return AdminDashboardStatsResponse
                .builder()

                .totalUsers(
                        userRepository.count()
                )

                .totalOwners(
                        userRepository.countByRole(
                                RoleType.OWNER
                        )
                )

                .totalApartments(
                        apartmentRepository.count()
                )

                .pendingApartments(
                        apartmentRepository
                                .countByApprovalStatus(
                                        ApartmentApprovalStatus.PENDING
                                )
                )

                .approvedApartments(
                        apartmentRepository
                                .countByApprovalStatus(
                                        ApartmentApprovalStatus.APPROVED
                                )
                )

                .rejectedApartments(
                        apartmentRepository
                                .countByApprovalStatus(
                                        ApartmentApprovalStatus.REJECTED
                                )
                )

                .totalInquiries(
                        inquiryRepository.count()
                )

                .build();
    }

    @Override
    public List<ApartmentResponse> getRecentPendingApartments() {
        return apartmentRepository
                .findTop5ByApprovalStatusOrderByCreatedAtDesc(
                        ApartmentApprovalStatus.PENDING
                )
                .stream()
                .map(this::mapToApartmentResponse)
                .toList();
    }

    @Override
    public List<ApartmentResponse> searchApartmentsForAdmin(String keyword) {
        return apartmentRepository
                .searchForAdmin(
                        keyword
                )
                .stream()
                .map(this::mapToApartmentResponse)
                .toList();
    }

    @Override
    public AdminOwnerDetailsResponse getOwnerDetails(Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() ->
                        new RuntimeException("Owner not found"));

        List<Apartment> apartments =
                apartmentRepository.findByOwner_Id(ownerId);

        List<ApartmentSummaryResponse> apartmentResponses =
                apartments.stream()
                        .map(apartment ->

                                ApartmentSummaryResponse.builder()
                                        .id(apartment.getId())
                                        .title(apartment.getTitle())
                                        .propertyType(apartment.getPropertyType())
                                        .purpose(apartment.getPurpose())
                                        .price(apartment.getPrice())
                                        .status(apartment.getStatus())
                                        .approvalStatus(
                                                apartment.getApprovalStatus()
                                        )
                                        .build()

                        )
                        .toList();

        UserResponse ownerResponse =
                UserResponse.builder()
                        .id(owner.getId())
                        .fullName(owner.getFullName())
                        .email(owner.getEmail())
                        .phone(owner.getPhone())
                        .role(owner.getRole())
                        .accountStatus(owner.getAccountStatus())
                        .verified(owner.isVerified())
                        .enabled(owner.isEnabled())
                        .build();

        return AdminOwnerDetailsResponse.builder()

                .owner(ownerResponse)

                .totalProperties(
                        apartmentRepository.countByOwner_Id(ownerId)
                )

                .approvedProperties(
                        apartmentRepository.countByOwner_IdAndApprovalStatus(
                                ownerId,
                                ApartmentApprovalStatus.APPROVED
                        )
                )

                .pendingProperties(
                        apartmentRepository.countByOwner_IdAndApprovalStatus(
                                ownerId,
                                ApartmentApprovalStatus.PENDING
                        )
                )

                .rejectedProperties(
                        apartmentRepository.countByOwner_IdAndApprovalStatus(
                                ownerId,
                                ApartmentApprovalStatus.REJECTED
                        )
                )

                .apartments(apartmentResponses)

                .build();
    }

    private ApartmentResponse mapToApartmentResponse(Apartment apartment) {

        ApartmentResponse response = new ApartmentResponse();

        response.setId(apartment.getId());
        response.setTitle(apartment.getTitle());
        response.setDescription(apartment.getDescription());
        response.setPurpose(apartment.getPurpose());
        response.setPropertyType(apartment.getPropertyType());
        response.setPrice(apartment.getPrice());
        response.setSizeSqFt(apartment.getSizeSqFt());
        response.setBedrooms(apartment.getBedrooms());
        response.setBathrooms(apartment.getBathrooms());
        response.setBalconies(apartment.getBalconies());
        response.setFloorNo(apartment.getFloorNo());
        response.setTotalFloor(apartment.getTotalFloor());
        response.setParkingSpaces(apartment.getParkingSpaces());
        response.setFurnishing(apartment.getFurnishing());
        response.setAddress(apartment.getAddress());
        response.setPostalCode(apartment.getPostalCode());
        response.setLatitude(apartment.getLatitude());
        response.setLongitude(apartment.getLongitude());
        response.setContactName(apartment.getContactName());
        response.setContactPhone(apartment.getContactPhone());
        response.setContactEmail(apartment.getContactEmail());
        response.setNegotiable(apartment.getNegotiable());
        response.setAvailable(apartment.getAvailable());
        response.setAvailableFrom(apartment.getAvailableFrom());
        response.setApprovalStatus(apartment.getApprovalStatus());
        response.setStatus(apartment.getStatus());
        response.setImages(
                apartment.getImages() == null
                        ? Collections.emptyList()
                        : apartment.getImages()
                          .stream()
                          .map(image ->
                               "http://localhost:8080/uploads/"
                               + image.getImageUrl()
                          )
                          .toList()
        );

        String coverImage = null;

        if (apartment.getImages() != null) {

            coverImage =
                    apartment.getImages()
                            .stream()
                            .filter(image ->
                                    Boolean.TRUE.equals(
                                            image.getCoverImage()
                                    )
                            )
                            .map(
                                    ApartmentImage::getImageUrl
                            )
                            .findFirst()
                            .orElse(null);
        }

        response.setCoverImage(
                coverImage
        );

        if (apartment.getLocation() != null) {

            response.setLocationId(
                    apartment.getLocation().getId()
            );

            response.setLocationName(
                    apartment.getLocation().getCity()
                            + ", "
                            + apartment.getLocation().getArea()
            );
        }

        response.setCreatedAt(
                apartment.getCreatedAt()
        );

        return response;

    }

}
