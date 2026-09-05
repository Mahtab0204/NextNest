package com.kgm.nextnest.dto;

import com.kgm.nextnest.model.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "Apartment Request Information"
)
public class ApartmentRequest {

    // =========================
    // BASIC INFORMATION
    // =========================

    private String title;

    private String description;


    // =========================
    // LISTING INFORMATION
    // =========================

    private Purpose purpose;

    private PropertyType propertyType;

    private ListingScope listingScope;


    // =========================
    // PRICE & SIZE
    // =========================

    private Double price;

    private Double sizeSqFt;


    // =========================
    // UNIT INFORMATION
    // =========================

    private Integer bedrooms;

    private Integer bathrooms;

    private Integer balconies;

    private Integer floorNo;

    private Integer totalFloor;


    // =========================
    // BUILDING INFORMATION
    // =========================

    private Integer totalUnits;

    private Double landAreaSqFt;

    private Integer yearBuilt;


    // =========================
    // COMMON INFORMATION
    // =========================

    private Integer parkingSpaces;

    private Furnishing furnishing;


    // =========================
    // ADDRESS
    // =========================

    private String address;

    private String postalCode;


    // =========================
    // LOCATION
    // =========================

    private Double latitude;

    private Double longitude;

    private Long locationId;


    // =========================
    // CONTACT
    // =========================

    private String contactName;

    private String contactPhone;

    private String contactEmail;


    // =========================
    // AVAILABILITY
    // =========================

    private Boolean negotiable;

    private Boolean available;

    private LocalDate availableFrom;


    // =========================
    // FEATURES
    // =========================

    private Set<PropertyFeature> features;
}