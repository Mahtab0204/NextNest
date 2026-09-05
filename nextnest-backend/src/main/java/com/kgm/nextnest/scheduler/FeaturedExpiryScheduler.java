package com.kgm.nextnest.scheduler;

import com.kgm.nextnest.model.Apartment;
import com.kgm.nextnest.repository.ApartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class FeaturedExpiryScheduler {

    private final ApartmentRepository apartmentRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void expireFeaturedApartments() {

        apartmentRepository
                .findAll()
                .forEach(apartment -> {

                    if (
                            apartment.getFeatured() != null
                                    && apartment.getFeatured()
                                    && apartment.getFeaturedEndDate() != null
                                    && apartment.getFeaturedEndDate()
                                    .isBefore(LocalDate.now())
                    ) {

                        apartment.setFeatured(false);

                        apartment.setFeaturedPlan(null);

                        apartment.setFeaturedStartDate(null);

                        apartment.setFeaturedEndDate(null);

                        apartmentRepository.save(apartment);
                    }

                });
    }
}