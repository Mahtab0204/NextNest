package com.kgm.nextnest.dto;

import com.kgm.nextnest.model.FeaturedPlan;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
        description = "Promote Apartment Request Information"
)
public class PromoteApartmentRequest {

    private Long apartmentId;

    private FeaturedPlan plan;

    private String transactionId;

    private String paymentMethod;
}