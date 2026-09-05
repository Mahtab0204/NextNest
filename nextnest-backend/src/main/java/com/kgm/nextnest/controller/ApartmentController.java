package com.kgm.nextnest.controller;

import com.kgm.nextnest.dto.ApartmentRequest;
import com.kgm.nextnest.dto.PromoteApartmentRequest;
import com.kgm.nextnest.model.Purpose;
import com.kgm.nextnest.response.ApartmentImageResponse;
import com.kgm.nextnest.response.ApartmentResponse;
import com.kgm.nextnest.response.FeatureApartmentResponse;
import com.kgm.nextnest.service.ApartmentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/apartments")
@RequiredArgsConstructor
@Tag(
        name = "CRUD REST APIs for Apartment Resource"
)
public class ApartmentController {

    private final ApartmentService apartmentService;

    // OWNER creates apartment
    @PostMapping
    public ResponseEntity<ApartmentResponse> createApartment(
            @RequestBody ApartmentRequest request,
            Authentication authentication
    ) {

        String ownerEmail = authentication.getName();

        ApartmentResponse response = apartmentService.createApartment(request, ownerEmail);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Get apartment by id
    @GetMapping("/{id}")
    public ResponseEntity<ApartmentResponse> getApartmentById(
            @PathVariable Long id
    ) {

        ApartmentResponse response = apartmentService.getApartmentById(id);

        return ResponseEntity.ok(response);
    }

    /*
    // Get all apartments
    @GetMapping
    public ResponseEntity<List<ApartmentResponse>> getAllApartments() {

        return ResponseEntity.ok(apartmentService.getAllApartments());
    }

     */

    // Update apartment
    @PutMapping("/{id}")
    public ResponseEntity<ApartmentResponse> updateApartment(
            @PathVariable Long id,
            @RequestBody ApartmentRequest request
    ) {

        ApartmentResponse response = apartmentService.updateApartment(id, request);

        return ResponseEntity.ok(response);
    }

    // Delete apartment
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteApartment(
            @PathVariable Long id
    ) {

        apartmentService.deleteApartment(id);
        return ResponseEntity.ok("Apartment deleted successfully");
    }

    // Owner apartments
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<ApartmentResponse>> getOwnerApartments(
            @PathVariable Long ownerId
    ) {

        return ResponseEntity.ok(apartmentService.getOwnerApartments(ownerId));
    }

    // Image Upload
    @PostMapping("/{id}/images")
    public ResponseEntity<String> uploadImage(
            @PathVariable Long id,
            @RequestParam("image")
            MultipartFile image
    ) {

        String fileName = apartmentService.uploadApartmentImage(id, image);

        return ResponseEntity.ok("Uploaded: " + fileName);
    }

    // Get Apartment Image

    @GetMapping("/{id}/images")
    public ResponseEntity<List<ApartmentImageResponse>> getApartmentImages(@PathVariable Long id) {

        return ResponseEntity.ok(apartmentService.getApartmentImages(id));
    }

    // delete Image
    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<String> deleteImage(
            @PathVariable Long imageId
    ) {

        apartmentService.deleteImage(imageId);

        return ResponseEntity.ok(
                "Image deleted successfully"
        );
    }

    // setCoverImage
    @PatchMapping("/images/{imageId}/cover")
    public ResponseEntity<String> setCoverImage(
            @PathVariable Long imageId
    ) {

        apartmentService.setCoverImage(imageId);

        return ResponseEntity.ok(
                "Cover image updated"
        );
    }

    // Search Apartment
    @GetMapping("/search")
    public ResponseEntity<List<ApartmentResponse>> searchApartments(
            @RequestParam String keyword) {
        return ResponseEntity.ok(apartmentService.searchApartments(keyword));
    }

    // Filter Price
    @GetMapping("/price")
    public ResponseEntity<List<ApartmentResponse>> getApartmentsByPriceRange(
            @RequestParam Double min,
            @RequestParam Double max) {

        return ResponseEntity.ok(apartmentService.getApartmentsByPriceRange(min, max));
    }

    // Filter Bedrooms
    @GetMapping("/bedrooms/{bedrooms}")
    public ResponseEntity<List<ApartmentResponse>>
    getApartmentsByBedrooms(
            @PathVariable Integer bedrooms){

        return ResponseEntity.ok(apartmentService.getApartmentsByBedrooms(bedrooms));
    }

    // Find Apartment By Purpose
    @GetMapping("/purpose/{purpose}")
    public ResponseEntity<List<ApartmentResponse>>
    getApartmentsByPurpose(
            @PathVariable Purpose purpose){

        return ResponseEntity.ok(apartmentService.getApartmentsByPurpose(purpose));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ApartmentResponse>> filterApartments(

            @RequestParam(required = false)
            Purpose purpose,
            @RequestParam(required = false)
            Integer bedrooms,
            @RequestParam(required = false)
            Double minPrice,
            @RequestParam(required = false)
            Double maxPrice) {

        return ResponseEntity.ok(apartmentService.filterApartments(purpose, bedrooms, minPrice, maxPrice));
    }

    // Find Apartments by pagination & sorting
    @GetMapping
    public ResponseEntity<Page<ApartmentResponse>>
    getAllApartments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        return ResponseEntity.ok(apartmentService.getAllApartments(page, size, sortBy, sortDir));
    }


    @GetMapping("/area")
    public ResponseEntity<List<ApartmentResponse>> getApartmentsByArea(
            @RequestParam String area,
            @RequestParam Purpose purpose ) {

        return ResponseEntity.ok(apartmentService.getApartmentsByArea(area, purpose));
    }

    @PostMapping("/feature")
    public ResponseEntity<FeatureApartmentResponse> featureApartment(
            @RequestBody PromoteApartmentRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(apartmentService.featureApartment(request, authentication.getName()));
    }

    @PostMapping("/promote")
    public ResponseEntity<ApartmentResponse> promoteApartment(
            @RequestBody
            PromoteApartmentRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(apartmentService.promoteApartment(request, authentication.getName()));
    }

    @GetMapping("/homepage")
    public ResponseEntity<List<ApartmentResponse>>
    getHomepageApartments() {

        return ResponseEntity.ok(
                apartmentService.getHomepageApartments()
        );
    }
}