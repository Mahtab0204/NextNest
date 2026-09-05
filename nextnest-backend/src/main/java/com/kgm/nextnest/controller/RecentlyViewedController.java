package com.kgm.nextnest.controller;

import com.kgm.nextnest.response.ApartmentResponse;
import com.kgm.nextnest.service.RecentlyViewedService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recently-viewed")
@RequiredArgsConstructor
@Tag(
        name = "CRUD REST APIs for Recently Viewed Resource"
)
public class RecentlyViewedController {

    private final RecentlyViewedService recentlyViewedService;

    @GetMapping
    public ResponseEntity<List<ApartmentResponse>> getRecentlyViewed(
            Authentication authentication) {

        return ResponseEntity.ok(recentlyViewedService.getRecentlyViewed(authentication.getName()));
    }
}