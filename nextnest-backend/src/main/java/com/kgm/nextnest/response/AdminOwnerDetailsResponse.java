package com.kgm.nextnest.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        description = "Admin Owner Details Response Information"
)
public class AdminOwnerDetailsResponse {

    private UserResponse owner;

    private Long totalProperties;

    private Long approvedProperties;

    private Long pendingProperties;

    private Long rejectedProperties;

    private List<ApartmentSummaryResponse> apartments;
}