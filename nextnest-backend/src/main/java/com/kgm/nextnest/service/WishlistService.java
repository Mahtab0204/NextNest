package com.kgm.nextnest.service;

import com.kgm.nextnest.response.ApartmentResponse;

import java.util.List;

public interface WishlistService {

    void addToWishlist(Long apartmentId, String customerEmail);

    void removeFromWishlist(Long apartmentId, String customerEmail);

    List<ApartmentResponse> getMyWishlist(String customerEmail);

    boolean isWishlisted(Long apartmentId, String customerEmail);
}