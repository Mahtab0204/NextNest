package com.kgm.nextnest.controller;

import com.kgm.nextnest.response.ApartmentResponse;
import com.kgm.nextnest.service.WishlistService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
@Tag(
        name = "CRUD REST APIs for Wishlist Resource"
)
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/{apartmentId}")
    public void addToWishlist(
            @PathVariable Long apartmentId,
            Authentication authentication
    ) {

        wishlistService.addToWishlist(
                apartmentId,
                authentication.getName()
        );
    }

    @DeleteMapping("/{apartmentId}")
    public void removeFromWishlist(
            @PathVariable Long apartmentId,
            Authentication authentication
    ) {

        wishlistService.removeFromWishlist(
                apartmentId,
                authentication.getName()
        );
    }

    @GetMapping("/my")
    public List<ApartmentResponse> getMyWishlist(
            Authentication authentication
    ) {

        return wishlistService.getMyWishlist(
                authentication.getName()
        );
    }

    @GetMapping("/check/{apartmentId}")
    public boolean isWishlisted(
            @PathVariable Long apartmentId,
            Authentication authentication
    ) {

        return wishlistService.isWishlisted(
                apartmentId,
                authentication.getName()
        );
    }
}