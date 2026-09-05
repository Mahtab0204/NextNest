package com.kgm.nextnest.service.impl;

import com.kgm.nextnest.dto.InquiryRequest;
import com.kgm.nextnest.exception.ResourceNotFoundException;
import com.kgm.nextnest.model.*;
import com.kgm.nextnest.repository.ApartmentRepository;
import com.kgm.nextnest.repository.InquiryRepository;
import com.kgm.nextnest.repository.UserRepository;
import com.kgm.nextnest.response.InquiryResponse;
import com.kgm.nextnest.service.InquiryService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {

    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;
    private final ApartmentRepository apartmentRepository;

    @Override
    public InquiryResponse createInquiry(InquiryRequest request, String customerEmail) {

        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Apartment apartment = apartmentRepository.findById(request.getApartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Apartment not found"));

        Inquiry inquiry = Inquiry.builder()
                .customer(customer)
                .apartment(apartment)
                .message(request.getMessage())
                .status(InquiryStatus.PENDING)
                .build();

        if (apartment.getOwner().getId().equals(customer.getId())) {

            throw new RuntimeException("You cannot send inquiry to your own apartment");
        }

        Inquiry savedInquiry = inquiryRepository.save(inquiry);

        return mapToResponse(savedInquiry);
    }

    @Override
    public List<InquiryResponse> getMyInquiries(String customerEmail) {

        User customer = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return inquiryRepository.findByCustomer_Id(customer.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<InquiryResponse> getOwnerInquiries(String ownerEmail) {

        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        return inquiryRepository.findByApartment_Owner_Id(owner.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    private InquiryResponse mapToResponse(Inquiry inquiry) {

        return InquiryResponse.builder()
                .id(inquiry.getId())
                .apartmentId(inquiry.getApartment().getId())
                .apartmentTitle(inquiry.getApartment().getTitle())
                .customerName(inquiry.getCustomer().getFullName())
                .customerEmail(inquiry.getCustomer().getEmail())
                .message(inquiry.getMessage())
                .status(inquiry.getStatus())
                .createdAt(inquiry.getCreatedAt())
                .build();
    }

    @Override
    public InquiryResponse updateInquiryStatus(Long inquiryId, InquiryStatus status, String ownerEmail) {

        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inquiry not found"));

        String apartmentOwnerEmail =
                inquiry.getApartment()
                        .getOwner()
                        .getEmail();

        if (!apartmentOwnerEmail.equals(ownerEmail)) {

            throw new ResourceNotFoundException("You are not authorized to update this inquiry");
        }

        inquiry.setStatus(status);

        Inquiry updatedInquiry = inquiryRepository.save(inquiry);

        return mapToResponse(updatedInquiry);
    }
}