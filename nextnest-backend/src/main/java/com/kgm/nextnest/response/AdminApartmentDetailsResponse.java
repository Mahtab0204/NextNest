package com.kgm.nextnest.response;

import com.kgm.nextnest.model.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "Admin Apartment Details Response Information"
)
public class AdminApartmentDetailsResponse {

    private Long id;

    private String title;

    private String description;

    private Double price;

    private Integer bedrooms;

    private Integer bathrooms;

    private Integer balconies;

    private Integer floorNo;

    private Double sizeSqFt;

    private String address;

    private Purpose purpose;

    private PropertyType propertyType;

    private Furnishing furnishing;

    private ApartmentStatus status;

    private ApartmentApprovalStatus approvalStatus;

    private String ownerName;

    private String ownerEmail;

    private String ownerPhone;

    private List<ApartmentImageResponse> images;
}