package com.kgm.nextnest.controller;

import com.kgm.nextnest.dto.RejectApartmentRequest;
import com.kgm.nextnest.response.AdminApartmentDetailsResponse;
import com.kgm.nextnest.response.ApartmentResponse;
import com.kgm.nextnest.service.ApartmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/apartments")
@RequiredArgsConstructor
@Tag(
        name = "CRUD REST APIs for Admin Apartment Resource"
)
public class AdminApartmentController {

    private final ApartmentService apartmentService;

    @GetMapping
    public ResponseEntity<List<ApartmentResponse>> getAllApartments() {

        return ResponseEntity.ok(apartmentService.getAllApartmentsForAdmin());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteApartment(
            @PathVariable Long id) {

        apartmentService.deleteApartmentByAdmin(id);

        return ResponseEntity.ok("Apartment deleted successfully");
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<ApartmentResponse> approveApartment(
            @PathVariable Long id) {

        return ResponseEntity.ok(apartmentService.approveApartment(id));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ApartmentResponse> rejectApartment(
            @PathVariable Long id,
            @RequestBody
            RejectApartmentRequest request) {

        return ResponseEntity.ok(apartmentService.rejectApartment(id, request.getReason()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminApartmentDetailsResponse> getApartmentDetails(
            @PathVariable Long id) {

        return ResponseEntity.ok(apartmentService.getApartmentDetailsForAdmin(id));
    }
}