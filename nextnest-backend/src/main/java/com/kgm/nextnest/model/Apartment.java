package com.kgm.nextnest.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "apartments")
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // OWNER
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "owner_id",
            nullable = false
    )
    private User owner;

    // BASIC INFORMATION

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;


    // LISTING INFORMATION

    @Enumerated(EnumType.STRING)
    private Purpose purpose;

    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    @Enumerated(EnumType.STRING)
    private ListingScope listingScope;


    // PRICE & SIZE

    @Column(nullable = false)
    private Double price;

    private Double sizeSqFt;


    // UNIT INFORMATION

    private Integer bedrooms;

    private Integer bathrooms;

    private Integer balconies;

    private Integer floorNo;

    private Integer totalFloor;



    // BUILDING INFORMATION


    private Integer totalUnits;

    private Double landAreaSqFt;

    private Integer yearBuilt;


    // COMMON PROPERTY INFORMATION

    private Integer parkingSpaces;

    @Enumerated(EnumType.STRING)
    private Furnishing furnishing;

    // STATUS


    @Enumerated(EnumType.STRING)
    private ApartmentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApartmentApprovalStatus approvalStatus;



    // ADDRESS

    private String address;

    private String postalCode;



    // LOCATION

    private Double latitude;

    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;


    // CONTACT

    private String contactName;

    private String contactPhone;

    private String contactEmail;


    // AVAILABILITY

    private Boolean negotiable;

    private Boolean available;

    private LocalDate availableFrom;

    // IMAGES

    @OneToMany(
            mappedBy = "apartment",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<ApartmentImage> images =
            new ArrayList<>();


    // FEATURES

    @ElementCollection(
            targetClass = PropertyFeature.class
    )
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "apartment_features",
            joinColumns =
            @JoinColumn(
                    name = "apartment_id"
            )
    )
    @Column(name = "feature")
    @Builder.Default
    private Set<PropertyFeature> features =
            new HashSet<>();


    // ADMIN

    @Column(length = 1000)
    private String rejectionReason;


    // TIMESTAMPS

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // FEATURED PROPERTY

    @Builder.Default
    private Boolean featured = false;

    @Enumerated(EnumType.STRING)
    private FeaturedPlan featuredPlan;

    private LocalDate featuredStartDate;

    private LocalDate featuredEndDate;
}