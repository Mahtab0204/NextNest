package com.kgm.nextnest.service.impl;

import com.kgm.nextnest.model.Apartment;
import com.kgm.nextnest.model.ApartmentImage;
import com.kgm.nextnest.model.User;
import com.kgm.nextnest.model.Wishlist;
import com.kgm.nextnest.repository.ApartmentRepository;
import com.kgm.nextnest.repository.UserRepository;
import com.kgm.nextnest.repository.WishlistRepository;
import com.kgm.nextnest.response.ApartmentResponse;
import com.kgm.nextnest.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;

    private final UserRepository userRepository;

    private final ApartmentRepository apartmentRepository;

    @Override
    public void addToWishlist(Long apartmentId, String customerEmail) {

        User customer = userRepository.findByEmail(customerEmail)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        Apartment apartment = apartmentRepository.findById(apartmentId)
                        .orElseThrow(() -> new RuntimeException("Apartment not found"));

        if (wishlistRepository.existsByCustomerAndApartment(customer, apartment)) {
            return;
        }

        Wishlist wishlist = Wishlist.builder()
                        .customer(customer)
                        .apartment(apartment)
                        .createdAt(LocalDateTime.now())
                        .build();

        wishlistRepository.save(wishlist);
    }

    @Override
    public void removeFromWishlist(Long apartmentId, String customerEmail) {

        User customer = userRepository.findByEmail(customerEmail)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        Apartment apartment = apartmentRepository.findById(apartmentId)
                        .orElseThrow(() -> new RuntimeException("Apartment not found"));

        Wishlist wishlist =
                wishlistRepository.findByCustomerAndApartment(customer,apartment)
                        .orElseThrow(() -> new RuntimeException("Wishlist item not found"));

        wishlistRepository.delete(wishlist);
    }

    @Override
    public List<ApartmentResponse> getMyWishlist(String customerEmail) {

        User customer = userRepository.findByEmail(customerEmail)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        return wishlistRepository.findByCustomer(customer)
                .stream()
                .map(wishlist -> mapToResponse(wishlist.getApartment()))
                .toList();
    }

    @Override
    public boolean isWishlisted(Long apartmentId, String customerEmail) {
        User customer = userRepository.findByEmail(customerEmail)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        Apartment apartment = apartmentRepository.findById(apartmentId)
                        .orElseThrow(() -> new RuntimeException("Apartment not found"));

        return wishlistRepository.existsByCustomerAndApartment(customer, apartment);
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