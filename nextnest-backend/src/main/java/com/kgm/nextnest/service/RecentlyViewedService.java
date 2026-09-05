package com.kgm.nextnest.service;

import com.kgm.nextnest.response.ApartmentResponse;

import java.util.List;

public interface RecentlyViewedService {

    void addView(Long apartmentId, String email);

    List<ApartmentResponse> getRecentlyViewed(String email);
}