package com.kgm.nextnest.controller;

import com.kgm.nextnest.service.FeaturedPaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/featured-payments")
@RequiredArgsConstructor
@Tag(
        name = "CRUD REST APIs for Featured Payment Resource"
)
public class FeaturedPaymentController {

    private final FeaturedPaymentService featuredPaymentService;

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<?> getOwnerHistory(
            @PathVariable Long ownerId) {

        return ResponseEntity.ok(featuredPaymentService.getOwnerFeaturedHistory(ownerId));
    }
}
