package com.kgm.nextnest.service.impl;

import com.kgm.nextnest.model.Apartment;
import com.kgm.nextnest.model.ApartmentImage;
import com.kgm.nextnest.model.RecentlyViewed;
import com.kgm.nextnest.model.User;
import com.kgm.nextnest.repository.ApartmentRepository;
import com.kgm.nextnest.repository.RecentlyViewedRepository;
import com.kgm.nextnest.repository.UserRepository;
import com.kgm.nextnest.response.ApartmentResponse;
import com.kgm.nextnest.service.RecentlyViewedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentlyViewedServiceImpl implements RecentlyViewedService {

    private final UserRepository userRepository;
    private final ApartmentRepository apartmentRepository;
    private final RecentlyViewedRepository recentlyViewedRepository;

    @Override
    public void addView(Long apartmentId, String email) {

        User customer = userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        Apartment apartment = apartmentRepository.findById(apartmentId)
                        .orElseThrow(() -> new RuntimeException("Apartment not found"));

        RecentlyViewed view = recentlyViewedRepository
                        .findByCustomerIdAndApartmentId(customer.getId(), apartmentId)
                        .orElse(null);

        if (view == null) {

            view = RecentlyViewed.builder()
                    .customer(customer)
                    .apartment(apartment)
                    .viewedAt(LocalDateTime.now())
                    .build();

        } else {

            view.setViewedAt(LocalDateTime.now());
        }

        recentlyViewedRepository.save(view);

        List<RecentlyViewed> views = recentlyViewedRepository.findByCustomerOrderByViewedAtDesc(customer);

        if (views.size() > 50) {

            List<RecentlyViewed> oldViews =
                    views.subList(
                            50,
                            views.size()
                    );

            recentlyViewedRepository.deleteAll(oldViews);
        }
    }

    @Override
    public List<ApartmentResponse>
    getRecentlyViewed(
            String email
    ) {

        User customer = userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        return recentlyViewedRepository.findTop10ByCustomerOrderByViewedAtDesc(customer)
                .stream()
                .map(view -> mapToResponse(view.getApartment()))
                .toList();
    }


    private ApartmentResponse mapToResponse(
            Apartment apartment
    ) {

        ApartmentResponse response = new ApartmentResponse();

        response.setId(apartment.getId());
        response.setTitle(apartment.getTitle());
        response.setDescription(apartment.getDescription());
        response.setPurpose(apartment.getPurpose());
        response.setPropertyType(apartment.getPropertyType());
        response.setPrice(apartment.getPrice());
        response.setSizeSqFt(apartment.getSizeSqFt());
        response.setBedrooms(apartment.getBedrooms());
        response.setBathrooms(apartment.getBathrooms());
        response.setBalconies(apartment.getBalconies());
        response.setFloorNo(apartment.getFloorNo());
        response.setTotalFloor(apartment.getTotalFloor());
        response.setParkingSpaces(apartment.getParkingSpaces());
        response.setFurnishing(apartment.getFurnishing());
        response.setAddress(apartment.getAddress());
        response.setPostalCode(apartment.getPostalCode());
        response.setLatitude(apartment.getLatitude());
        response.setLongitude(apartment.getLongitude());
        response.setContactName(apartment.getContactName());
        response.setContactPhone(apartment.getContactPhone());
        response.setContactEmail(apartment.getContactEmail());
        response.setNegotiable(apartment.getNegotiable());
        response.setAvailable(apartment.getAvailable());
        response.setAvailableFrom(apartment.getAvailableFrom());
        response.setApprovalStatus(apartment.getApprovalStatus());
        response.setStatus(apartment.getStatus());
        response.setRejectionReason(apartment.getRejectionReason());
        response.setImages(
                apartment.getImages() == null
                        ? Collections.emptyList()
                        : apartment.getImages()
                          .stream()
                          .map(image ->
                               "http://localhost:8080/uploads/"
                               + image.getImageUrl()
                          )
                          .toList()
        );

        String coverImage = null;

        if (apartment.getImages() != null) {

            coverImage = apartment.getImages()
                    .stream()
                    .filter(image ->
                            Boolean.TRUE.equals(
                                    image.getCoverImage()
                            )
                    )
                    .map(
                            ApartmentImage::getImageUrl
                    )
                    .findFirst()
                    .orElse(null);
        }

        response.setCoverImage(
                coverImage
        );

        if (apartment.getLocation() != null) {

            response.setLocationId(apartment.getLocation().getId());

            response.setLocationName(
                    apartment.getLocation().getCity()
                            + ", "
                            + apartment.getLocation().getArea()
            );
        }

        response.setCreatedAt(apartment.getCreatedAt());

        return response;
    }
}