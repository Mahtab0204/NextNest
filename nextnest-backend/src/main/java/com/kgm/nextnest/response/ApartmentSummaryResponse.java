package com.kgm.nextnest.response;

import com.kgm.nextnest.model.ApartmentApprovalStatus;
import com.kgm.nextnest.model.ApartmentStatus;
import com.kgm.nextnest.model.PropertyType;
import com.kgm.nextnest.model.Purpose;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        description = "Apartment Summary Response Information"
)
public class ApartmentSummaryResponse {

    private Long id;

    private String title;

    private PropertyType propertyType;

    private Purpose purpose;

    private Double price;

    private ApartmentStatus status;

    private ApartmentApprovalStatus approvalStatus;
}