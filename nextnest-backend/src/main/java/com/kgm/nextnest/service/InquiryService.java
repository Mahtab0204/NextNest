package com.kgm.nextnest.service;

import com.kgm.nextnest.dto.InquiryRequest;
import com.kgm.nextnest.model.InquiryStatus;
import com.kgm.nextnest.response.InquiryResponse;

import java.util.List;

public interface InquiryService {

    InquiryResponse createInquiry(InquiryRequest request, String customerEmail);

    List<InquiryResponse> getMyInquiries(String customerEmail);

    List<InquiryResponse> getOwnerInquiries(String ownerEmail);

    InquiryResponse updateInquiryStatus(Long inquiryId, InquiryStatus status, String ownerEmail);
}