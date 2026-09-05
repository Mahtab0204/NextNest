package com.kgm.nextnest.service.impl;

import com.kgm.nextnest.dto.ApartmentRequest;
import com.kgm.nextnest.dto.PromoteApartmentRequest;
import com.kgm.nextnest.model.*;
import com.kgm.nextnest.repository.*;
import com.kgm.nextnest.response.*;
import com.kgm.nextnest.service.ApartmentService;
import com.kgm.nextnest.service.FileStorageService;
import com.kgm.nextnest.service.RecentlyViewedService;
import com.kgm.nextnest.specification.ApartmentSpecification;
import com.kgm.nextnest.util.FeaturedPricing;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApartmentServiceImpl implements ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final ModelMapper modelMapper;
    private final ApartmentImageRepository apartmentImageRepository;
    private final FileStorageService fileStorageService;
    private final RecentlyViewedService recentlyViewedService;
    private final FeaturedPaymentRepository featuredPaymentRepository;

    @Override
    public ApartmentResponse createApartment(
            ApartmentRequest request,
            String ownerEmail
    ) {

        validateListing(request);

        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() ->
                        new RuntimeException("Owner not found"));

        Location location = locationRepository.findById(
                        request.getLocationId())
                .orElseThrow(() ->
                        new RuntimeException("Location not found"));

        Apartment apartment = new Apartment();

        // Basic information
        apartment.setTitle(request.getTitle());
        apartment.setDescription(request.getDescription());

        // Listing information
        apartment.setPurpose(request.getPurpose());
        apartment.setListingScope(request.getListingScope());
        apartment.setPropertyType(request.getPropertyType());

        // Pricing and size
        apartment.setPrice(request.getPrice());
        apartment.setSizeSqFt(request.getSizeSqFt());

        // Unit-specific information
        apartment.setBedrooms(request.getBedrooms());
        apartment.setBathrooms(request.getBathrooms());
        apartment.setBalconies(request.getBalconies());
        apartment.setFloorNo(request.getFloorNo());

        // Building / common information
        apartment.setTotalFloor(request.getTotalFloor());
        apartment.setTotalUnits(request.getTotalUnits());
        apartment.setLandAreaSqFt(request.getLandAreaSqFt());
        apartment.setParkingSpaces(request.getParkingSpaces());
        apartment.setYearBuilt(request.getYearBuilt());

        // Furnishing
        apartment.setFurnishing(request.getFurnishing());

        // Address
        apartment.setAddress(request.getAddress());
        apartment.setPostalCode(request.getPostalCode());

        // Location coordinates
        apartment.setLatitude(request.getLatitude());
        apartment.setLongitude(request.getLongitude());

        // Contact information
        apartment.setContactName(request.getContactName());
        apartment.setContactPhone(request.getContactPhone());
        apartment.setContactEmail(request.getContactEmail());

        // Availability
        apartment.setNegotiable(request.getNegotiable());
        apartment.setAvailable(request.getAvailable());
        apartment.setAvailableFrom(request.getAvailableFrom());

        // Features
        apartment.setFeatures(request.getFeatures());

        // Relationships
        apartment.setOwner(owner);
        apartment.setLocation(location);

        // Initial status
        apartment.setStatus(
                ApartmentStatus.AVAILABLE
        );

        apartment.setApprovalStatus(
                ApartmentApprovalStatus.PENDING
        );

        Apartment savedApartment =
                apartmentRepository.save(apartment);

        return mapToResponse(savedApartment);
    }

    @Override
    public ApartmentResponse getApartmentById(Long id) {

        Apartment apartment = apartmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Apartment not found"));

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (
                authentication != null &&
                        authentication.isAuthenticated() &&
                        !"anonymousUser".equals(
                                authentication.getName()
                        )
        ) {

            User user = userRepository.findByEmail(authentication.getName()).orElse(null);

            if (user != null && user.getRole() == RoleType.CUSTOMER) {

                recentlyViewedService.addView(id, user.getEmail()
                );
            }
        }

        return mapToResponse(apartment);
    }

    @Override
    public List<ApartmentResponse> getAllApartments() {

        return apartmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ApartmentResponse updateApartment(
            Long id,
            ApartmentRequest request
    ) {

        validateListing(request);

        Apartment apartment = apartmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Apartment not found"));

        // Update location only when provided
        if (request.getLocationId() != null) {

            Location location =
                    locationRepository.findById(
                                    request.getLocationId()
                            )
                            .orElseThrow(() ->
                                    new RuntimeException("Location not found"));

            apartment.setLocation(location);
        }

        // Basic information
        apartment.setTitle(request.getTitle());
        apartment.setDescription(request.getDescription());

        // Listing information
        apartment.setPurpose(request.getPurpose());
        apartment.setListingScope(request.getListingScope());
        apartment.setPropertyType(request.getPropertyType());

        // Pricing and size
        apartment.setPrice(request.getPrice());
        apartment.setSizeSqFt(request.getSizeSqFt());

        // Unit-specific information
        apartment.setBedrooms(request.getBedrooms());
        apartment.setBathrooms(request.getBathrooms());
        apartment.setBalconies(request.getBalconies());
        apartment.setFloorNo(request.getFloorNo());

        // Building / common information
        apartment.setTotalFloor(request.getTotalFloor());
        apartment.setTotalUnits(request.getTotalUnits());
        apartment.setLandAreaSqFt(request.getLandAreaSqFt());
        apartment.setParkingSpaces(request.getParkingSpaces());
        apartment.setYearBuilt(request.getYearBuilt());

        // Furnishing
        apartment.setFurnishing(request.getFurnishing());

        // Address
        apartment.setAddress(request.getAddress());
        apartment.setPostalCode(request.getPostalCode());

        // Coordinates
        apartment.setLatitude(request.getLatitude());
        apartment.setLongitude(request.getLongitude());

        // Contact information
        apartment.setContactName(request.getContactName());
        apartment.setContactPhone(request.getContactPhone());
        apartment.setContactEmail(request.getContactEmail());

        // Availability
        apartment.setNegotiable(request.getNegotiable());
        apartment.setAvailable(request.getAvailable());
        apartment.setAvailableFrom(request.getAvailableFrom());

        // Features
        apartment.setFeatures(request.getFeatures());

        Apartment updatedApartment =
                apartmentRepository.save(apartment);

        return mapToResponse(updatedApartment);
    }

    @Override
    public void deleteApartment(Long id) {

        Apartment apartment = apartmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Apartment not found"));

        apartmentRepository.delete(apartment);
    }

    @Override
    public List<ApartmentResponse> getOwnerApartments(
            Long ownerId
    ) {

        return apartmentRepository
                .findByOwner_Id(ownerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }



/*
    private ApartmentResponse mapToResponse(Apartment apartment) {

        ApartmentResponse response =
                modelMapper.map(apartment, ApartmentResponse.class);

        if (apartment.getLocation() != null) {

            response.setLocationName(
                    apartment.getLocation().getCity()
                            + ", "
                            + apartment.getLocation().getArea()
            );
        }

        return response;
    }

 */

    private ApartmentResponse mapToResponse(
            Apartment apartment
    ) {

        ApartmentResponse response =
                new ApartmentResponse();

        // Basic information
        response.setId(apartment.getId());
        response.setTitle(apartment.getTitle());
        response.setDescription(apartment.getDescription());

        // Listing information
        response.setPurpose(apartment.getPurpose());
        response.setListingScope(apartment.getListingScope());
        response.setPropertyType(apartment.getPropertyType());

        // Pricing and size
        response.setPrice(apartment.getPrice());
        response.setSizeSqFt(apartment.getSizeSqFt());

        // Unit information
        response.setBedrooms(apartment.getBedrooms());
        response.setBathrooms(apartment.getBathrooms());
        response.setBalconies(apartment.getBalconies());
        response.setFloorNo(apartment.getFloorNo());

        // Building / common information
        response.setTotalFloor(apartment.getTotalFloor());
        response.setTotalUnits(apartment.getTotalUnits());
        response.setLandAreaSqFt(apartment.getLandAreaSqFt());
        response.setParkingSpaces(apartment.getParkingSpaces());
        response.setYearBuilt(apartment.getYearBuilt());

        // Furnishing
        response.setFurnishing(apartment.getFurnishing());

        // Address
        response.setAddress(apartment.getAddress());
        response.setPostalCode(apartment.getPostalCode());

        // Coordinates
        response.setLatitude(apartment.getLatitude());
        response.setLongitude(apartment.getLongitude());

        // Contact information
        response.setContactName(apartment.getContactName());
        response.setContactPhone(apartment.getContactPhone());
        response.setContactEmail(apartment.getContactEmail());

        // Availability
        response.setNegotiable(apartment.getNegotiable());
        response.setAvailable(apartment.getAvailable());
        response.setAvailableFrom(apartment.getAvailableFrom());

        // Status
        response.setApprovalStatus(
                apartment.getApprovalStatus()
        );

        response.setStatus(
                apartment.getStatus()
        );

        response.setRejectionReason(
                apartment.getRejectionReason()
        );

        // Owner
        if (apartment.getOwner() != null) {

            response.setOwnerId(
                    apartment.getOwner().getId()
            );
        }

        // Features
        response.setFeatures(apartment.getFeatures());
        // Featured Information

        response.setFeatured(apartment.getFeatured() != null
                        ? apartment.getFeatured()
                        : false
        );

        response.setFeaturedPlan(apartment.getFeaturedPlan());

        response.setFeaturedStartDate(apartment.getFeaturedStartDate());

        response.setFeaturedEndDate(apartment.getFeaturedEndDate());
        // Images
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

        // Cover image
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
                                    image ->
                                            "http://localhost:8080/uploads/"
                                                    + image.getImageUrl()
                            )
                            .findFirst()
                            .orElse(null);
        }

        response.setCoverImage(
                coverImage
        );

        // Location
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

        // Created date
        response.setCreatedAt(
                apartment.getCreatedAt()
        );

        return response;
    }
    @Override
    public String uploadApartmentImage(Long apartmentId, MultipartFile image) {

        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new RuntimeException("Apartment not found"));

        try {

            String fileName = fileStorageService.saveFile(image);

            ApartmentImage apartmentImage =
                    ApartmentImage.builder()
                            .imageUrl(fileName)
                            .apartment(apartment)
                            .build();

            apartmentImageRepository.save(apartmentImage);

            return fileName;

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
    }

    @Override
    public List<ApartmentResponse> searchApartments(String keyword) {
        return apartmentRepository
                .searchFeaturedFirst(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ApartmentResponse> getApartmentsByPriceRange(Double minPrice, Double maxPrice) {

        if(minPrice > maxPrice){
            throw new RuntimeException("Minimum price cannot be greater than maximum price");
        }
        return apartmentRepository.findByPriceBetween(minPrice, maxPrice)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ApartmentResponse> getApartmentsByBedrooms(Integer bedrooms) {
        return apartmentRepository
                .findByBedrooms(bedrooms)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ApartmentResponse> getApartmentsByPurpose(Purpose purpose) {
        return apartmentRepository
                .findByPurposeFeaturedFirst(purpose)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ApartmentResponse> filterApartments(
            Purpose purpose,
            Integer bedrooms,
            Double minPrice,
            Double maxPrice
    ) {

        Sort sort = Sort.by(
                Sort.Order.desc("featured"),
                Sort.Order.desc("createdAt")
        );

        return apartmentRepository
                .findAll(
                        ApartmentSpecification.filterApartments(
                                purpose,
                                bedrooms,
                                minPrice,
                                maxPrice
                        ),
                        sort
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public Page<ApartmentResponse> getAllApartments(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Apartment> apartmentPage = apartmentRepository.findAll(pageable);

        return apartmentPage.map(this::mapToResponse);
    }

    @Override
    public List<ApartmentImageResponse> getApartmentImages(
            Long apartmentId) {

        Apartment apartment = apartmentRepository.findById(apartmentId)
                        .orElseThrow(() -> new RuntimeException("Apartment not found"));

        return apartment
                .getImages()
                .stream()
                .map(image ->
                        new ApartmentImageResponse(
                                image.getId(),
                                image.getImageUrl(),
                                image.getCoverImage()
                        )
                )
                .toList();
    }

    @Override
    public void deleteImage(Long imageId) {

        ApartmentImage image = apartmentImageRepository.findById(imageId)
                        .orElseThrow(() -> new RuntimeException("Image not found"));

        try {

            fileStorageService.deleteFile(image.getImageUrl());

        } catch (IOException e) {

            throw new RuntimeException("Failed to delete image file");
        }

        apartmentImageRepository.delete(image);
    }

    @Override
    public void setCoverImage(Long imageId) {

        ApartmentImage selectedImage = apartmentImageRepository.findById(imageId)
                        .orElseThrow(() -> new RuntimeException("Image not found"));

        Apartment apartment = selectedImage.getApartment();

        apartment.getImages().forEach(image -> image.setCoverImage(false));

        selectedImage.setCoverImage(true);

        apartmentImageRepository.saveAll(apartment.getImages());
    }

    @Override
    public List<ApartmentResponse>
    getAllApartmentsForAdmin() {

        return apartmentRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteApartmentByAdmin(
            Long id
    ) {

        Apartment apartment =
                apartmentRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Apartment not found"
                                ));

        apartmentRepository.delete(
                apartment
        );
    }

    @Override
    public ApartmentResponse approveApartment(Long id) {
        Apartment apartment = apartmentRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Apartment not found"));

        apartment.setApprovalStatus(ApartmentApprovalStatus.APPROVED);

        return mapToResponse(apartmentRepository.save(apartment));
    }

    @Override
    public ApartmentResponse rejectApartment(Long id, String reason) {
        Apartment apartment =
                apartmentRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Apartment not found"
                                )
                        );

        apartment.setApprovalStatus(
                ApartmentApprovalStatus.REJECTED
        );

        apartment.setRejectionReason(
                reason
        );

        return mapToResponse(
                apartmentRepository.save(
                        apartment
                )
        );
    }

    @Override
    public AdminApartmentDetailsResponse getApartmentDetailsForAdmin(Long id) {
        Apartment apartment =
                apartmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Apartment not found"
                                )
                        );

        return AdminApartmentDetailsResponse
                .builder()

                .id(apartment.getId())
                .title(apartment.getTitle())
                .description(apartment.getDescription())
                .price(apartment.getPrice())
                .bedrooms(apartment.getBedrooms())
                .bathrooms(apartment.getBathrooms())
                .balconies(apartment.getBalconies())
                .floorNo(apartment.getFloorNo())
                .sizeSqFt(apartment.getSizeSqFt())
                .address(apartment.getAddress())
                .purpose(apartment.getPurpose())
                .propertyType(apartment.getPropertyType())
                .furnishing(apartment.getFurnishing())
                .status(apartment.getStatus())
                .approvalStatus(apartment.getApprovalStatus())
                .ownerName(apartment.getOwner().getFullName())
                .ownerEmail(apartment.getOwner().getEmail())
                .ownerPhone(apartment.getOwner().getPhone())
                .images(getApartmentImages(apartment.getId()))
                .build();
    }

    @Override
    public List<ApartmentResponse> getApartmentsByApprovalStatus(ApartmentApprovalStatus status) {
        return apartmentRepository
                .findByApprovalStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ApartmentResponse> getApartmentsByArea(String area, Purpose purpose) {
        List<Apartment> apartments = apartmentRepository
                .findByAreaFeaturedFirst(
                        area,
                        purpose
                );

        return apartments.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public FeatureApartmentResponse featureApartment(PromoteApartmentRequest request, String ownerEmail) {
        Apartment apartment =
                apartmentRepository.findById(
                        request.getApartmentId()
                ).orElseThrow(
                        () -> new RuntimeException("Apartment not found")
                );

        User owner =
                userRepository.findByEmail(ownerEmail)
                        .orElseThrow(
                                () -> new RuntimeException("Owner not found")
                        );

        if (!apartment.getOwner().getId().equals(owner.getId())) {

            throw new RuntimeException(
                    "You can feature only your own property"
            );
        }

        int days;
        double amount;

        switch (request.getPlan()) {

            case SEVEN_DAYS -> {
                days = 7;
                amount = 300;
            }

            case FIFTEEN_DAYS -> {
                days = 15;
                amount = 500;
            }

            case THIRTY_DAYS -> {
                days = 30;
                amount = 1000;
            }

            default ->
                    throw new RuntimeException("Invalid plan");
        }

        apartment.setFeatured(true);

        apartment.setFeaturedPlan(
                request.getPlan()
        );

        apartment.setFeaturedStartDate(
                LocalDate.now()
        );

        apartment.setFeaturedEndDate(
                LocalDate.now().plusDays(days)
        );

        apartmentRepository.save(apartment);

        FeaturedPayment payment =
                FeaturedPayment.builder()
                        .apartment(apartment)
                        .owner(owner)
                        .plan(request.getPlan())
                        .amount(amount)
                        .paymentMethod("MANUAL")
                        .transactionId("TXN-" + System.currentTimeMillis())
                        .paid(true)
                        .paidAt(LocalDateTime.now())
                        .build();

        featuredPaymentRepository.save(payment);

        return FeatureApartmentResponse.builder()
                .apartmentId(apartment.getId())
                .featured(true)
                .plan(request.getPlan().name())
                .expiresAt(
                        apartment.getFeaturedEndDate().toString()
                )
                .amount(amount)
                .build();
    }

    @Override
    public ApartmentResponse promoteApartment(PromoteApartmentRequest request, String ownerEmail) {
        Apartment apartment =
                apartmentRepository
                        .findById(
                                request.getApartmentId()
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Apartment not found"
                                )
                        );

        User owner =
                userRepository
                        .findByEmail(
                                ownerEmail
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                )
                        );

        LocalDate startDate =
                LocalDate.now();

        LocalDate endDate;

        switch (request.getPlan()) {

            case SEVEN_DAYS ->
                    endDate =
                            startDate.plusDays(7);

            case FIFTEEN_DAYS ->
                    endDate =
                            startDate.plusDays(15);

            case THIRTY_DAYS ->
                    endDate =
                            startDate.plusDays(30);

            default ->
                    throw new RuntimeException(
                            "Invalid Plan"
                    );
        }

        apartment.setFeatured(true);

        apartment.setFeaturedPlan(
                request.getPlan()
        );

        apartment.setFeaturedStartDate(
                startDate
        );

        apartment.setFeaturedEndDate(
                endDate
        );

        apartmentRepository.save(
                apartment
        );

        FeaturedPayment payment =
                FeaturedPayment.builder()
                        .apartment(apartment)
                        .owner(owner)
                        .plan(request.getPlan())
                        .amount(
                                FeaturedPricing
                                        .getPrice(
                                                request.getPlan()
                                        )
                        )
                        .paymentMethod(
                                request.getPaymentMethod()
                        )
                        .transactionId(
                                request.getTransactionId()
                        )
                        .paid(true)
                        .paidAt(
                                LocalDateTime.now()
                        )
                        .build();

        featuredPaymentRepository.save(
                payment
        );

        return mapToResponse(
                apartment
        );
    }

    @Override
    public List<ApartmentResponse> getHomepageApartments() {
        return apartmentRepository
                .findHomepageApartments()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    private void validateListing(ApartmentRequest request) {

        if (request.getListingScope() == null) {
            throw new IllegalArgumentException(
                    "Listing scope is required"
            );
        }

        if (request.getPropertyType() == null) {
            throw new IllegalArgumentException(
                    "Property type is required"
            );
        }

        // BUILDING VALIDATION

        if (request.getListingScope() == ListingScope.BUILDING) {

            if (request.getPropertyType() != PropertyType.APARTMENT) {
                throw new IllegalArgumentException(
                        "An entire building must use APARTMENT property type"
                );
            }

            if (request.getTotalFloor() == null ||
                    request.getTotalFloor() <= 0) {

                throw new IllegalArgumentException(
                        "Total floors is required for a building"
                );
            }

            if (request.getTotalUnits() == null ||
                    request.getTotalUnits() <= 0) {

                throw new IllegalArgumentException(
                        "Total units is required for a building"
                );
            }

            if (request.getLandAreaSqFt() == null ||
                    request.getLandAreaSqFt() <= 0) {

                throw new IllegalArgumentException(
                        "Land area is required for a building"
                );
            }

            // Building does not use unit-level information
            request.setBedrooms(null);
            request.setBathrooms(null);
            request.setBalconies(null);
            request.setFloorNo(null);
            request.setFurnishing(null);
        }


        // UNIT VALIDATION

        if (request.getListingScope() == ListingScope.UNIT) {

            if (request.getPropertyType() ==
                    PropertyType.APARTMENT) {

                throw new IllegalArgumentException(
                        "APARTMENT is reserved for entire building listings"
                );
            }

            if (request.getSizeSqFt() == null ||
                    request.getSizeSqFt() <= 0) {

                throw new IllegalArgumentException(
                        "Property size is required"
                );
            }

            // Building-only fields should not be stored
            request.setTotalUnits(null);
            request.setLandAreaSqFt(null);
            request.setYearBuilt(null);
        }
    }
}