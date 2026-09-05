package com.kgm.nextnest.service;

import com.kgm.nextnest.response.AdminDashboardStatsResponse;
import com.kgm.nextnest.response.AdminOwnerDetailsResponse;
import com.kgm.nextnest.response.ApartmentResponse;

import java.util.List;

public interface AdminService {

    AdminDashboardStatsResponse getDashboardStats();

    List<ApartmentResponse>
    getRecentPendingApartments();

    List<ApartmentResponse>
    searchApartmentsForAdmin(
            String keyword
    );

    AdminOwnerDetailsResponse getOwnerDetails(Long ownerId);
}