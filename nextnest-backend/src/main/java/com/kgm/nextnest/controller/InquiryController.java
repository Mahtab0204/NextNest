package com.kgm.nextnest.controller;

import com.kgm.nextnest.dto.InquiryRequest;
import com.kgm.nextnest.dto.UpdateInquiryStatusRequest;
import com.kgm.nextnest.response.InquiryResponse;
import com.kgm.nextnest.service.InquiryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
@Tag(
        name = "CRUD REST APIs for Inquiry Resource"
)
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping
    public ResponseEntity<InquiryResponse> createInquiry(
            @RequestBody InquiryRequest request,
            Authentication authentication) {

        String customerEmail = authentication.getName();

        InquiryResponse response = inquiryService.createInquiry(request, customerEmail);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/my")
    public ResponseEntity<List<InquiryResponse>> getMyInquiries(Authentication authentication) {

        String customerEmail = authentication.getName();

        return ResponseEntity.ok(inquiryService.getMyInquiries(customerEmail));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<InquiryResponse>> getOwnerInquiries(Authentication authentication) {

        String ownerEmail = authentication.getName();

        return ResponseEntity.ok(inquiryService.getOwnerInquiries(ownerEmail));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<InquiryResponse> updateInquiryStatus(

            @PathVariable Long id,
            @RequestBody UpdateInquiryStatusRequest request,
            Authentication authentication) {

        String ownerEmail = authentication.getName();

        return ResponseEntity.ok(inquiryService.updateInquiryStatus(id, request.getStatus(), ownerEmail));
    }
}