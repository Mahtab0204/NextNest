package com.kgm.nextnest.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(
        description = "Feature Apartment Response Information"
)
public class FeatureApartmentResponse {

    private Long apartmentId;

    private Boolean featured;

    private String plan;

    private String expiresAt;

    private Double amount;
}