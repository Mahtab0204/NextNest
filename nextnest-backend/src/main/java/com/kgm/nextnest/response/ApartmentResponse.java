package com.kgm.nextnest.response;

import com.kgm.nextnest.model.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        description = "Apartment Response Information"
)
public class ApartmentResponse {

    private Long id;

    private String title;

    private String description;

    private Purpose purpose;

    private PropertyType propertyType;

    private ListingScope listingScope;

    private Double price;

    private Double sizeSqFt;

    private Integer bedrooms;

    private Integer bathrooms;

    private Integer balconies;

    private Integer floorNo;

    private Integer totalFloor;

    private Integer totalUnits;

    private Double landAreaSqFt;

    private Integer yearBuilt;

    private Integer parkingSpaces;

    private Furnishing furnishing;

    private String address;

    private String postalCode;

    private Long locationId;

    private String locationName;

    private Double latitude;

    private Double longitude;

    private String contactName;

    private String contactPhone;

    private String contactEmail;

    private Boolean negotiable;

    private Boolean available;

    private LocalDate availableFrom;

    private ApartmentStatus status;

    private ApartmentApprovalStatus approvalStatus;

    private String rejectionReason;

    private Long ownerId;

    private Set<PropertyFeature> features;

    private List<String> images;

    private String coverImage;

    private LocalDateTime createdAt;

    private Boolean featured;

    private FeaturedPlan featuredPlan;

    private LocalDate featuredStartDate;

    private LocalDate featuredEndDate;
}