package com.kgm.nextnest.controller;

import com.kgm.nextnest.dto.AiSearchRequest;
import com.kgm.nextnest.dto.ApartmentRequest;
import com.kgm.nextnest.service.AiApartmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
@Tag(
        name = "CRUD REST APIs for AI Resource"
)
public class AiController {

    private final AiApartmentService aiApartmentService;

    public AiController(AiApartmentService aiApartmentService) {
        this.aiApartmentService = aiApartmentService;
    }

    @PostMapping("/generate-description")
    public ResponseEntity<String> generateDescription(@RequestBody ApartmentRequest request) {
        String aiDescription = aiApartmentService.generateDescription(request);
        return ResponseEntity.ok(aiDescription);
    }

    @GetMapping("/smart-search")
    public ResponseEntity<AiSearchRequest> smartSearch(@RequestParam String query) {
        AiSearchRequest extractedParams = aiApartmentService.parseSearchQuery(query);
        return ResponseEntity.ok(extractedParams);
    }
}