package com.kgm.nextnest.response;

import com.kgm.nextnest.model.FeaturedPlan;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        description = "Feature Payment Response Information"
)
public class FeaturedPaymentResponse {

    private Long id;

    private Long apartmentId;

    private String apartmentTitle;

    private FeaturedPlan plan;

    private Double amount;

    private String paymentMethod;

    private String transactionId;

    private Boolean paid;

    private LocalDateTime paidAt;
}