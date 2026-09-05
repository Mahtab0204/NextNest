package com.kgm.nextnest.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "Admin Dashboard Stats Response Information"
)
public class AdminDashboardStatsResponse {

    private Long totalUsers;

    private Long totalOwners;

    private Long totalApartments;

    private Long pendingApartments;

    private Long approvedApartments;

    private Long rejectedApartments;

    private Long totalInquiries;
}