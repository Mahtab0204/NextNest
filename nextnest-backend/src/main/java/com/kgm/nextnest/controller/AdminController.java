package com.kgm.nextnest.controller;


import com.kgm.nextnest.model.ApartmentApprovalStatus;
import com.kgm.nextnest.response.AdminDashboardStatsResponse;
import com.kgm.nextnest.response.AdminOwnerDetailsResponse;
import com.kgm.nextnest.response.ApartmentResponse;
import com.kgm.nextnest.service.AdminService;
import com.kgm.nextnest.service.ApartmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(
        name = "CRUD REST APIs for Admin Resource"
)
public class AdminController {

    private final AdminService adminService;
    private final ApartmentService apartmentService;

    @GetMapping("/dashboard/stats")
    public ResponseEntity<AdminDashboardStatsResponse>getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @GetMapping("/apartments/pending/recent")
    public ResponseEntity<
            List<ApartmentResponse>
            > getRecentPendingApartments() {

        return ResponseEntity.ok(
                adminService
                        .getRecentPendingApartments()
        );
    }

    @GetMapping("/apartments/search")
    public ResponseEntity<List<ApartmentResponse>> searchApartments(
            @RequestParam String keyword) {

        return ResponseEntity.ok(adminService.searchApartmentsForAdmin(keyword));
    }

    @GetMapping("/apartments/status/{status}")
    public ResponseEntity<List<ApartmentResponse>> getApartmentsByStatus(
            @PathVariable
            ApartmentApprovalStatus status) {

        return ResponseEntity.ok(apartmentService.getApartmentsByApprovalStatus(status));
    }

    @GetMapping("/owners/{id}")
    public ResponseEntity<AdminOwnerDetailsResponse>
    getOwnerDetails(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                adminService.getOwnerDetails(id)
        );
    }
}