package com.kgm.nextnest.service;

import com.kgm.nextnest.response.FeaturedPaymentResponse;

import java.util.List;

public interface FeaturedPaymentService {

    List<FeaturedPaymentResponse>
    getOwnerFeaturedHistory(Long ownerId);
}