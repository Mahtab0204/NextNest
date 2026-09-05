package com.kgm.nextnest.repository;


import com.kgm.nextnest.model.Apartment;
import com.kgm.nextnest.model.User;
import com.kgm.nextnest.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findByCustomer(User customer);

    Optional<Wishlist> findByCustomerAndApartment(User customer, Apartment apartment);

    boolean existsByCustomerAndApartment(User customer, Apartment apartment);

    void deleteByCustomerAndApartment(User customer, Apartment apartment);
}