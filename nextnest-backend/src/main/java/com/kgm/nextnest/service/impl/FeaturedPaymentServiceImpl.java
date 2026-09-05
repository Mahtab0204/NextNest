package com.kgm.nextnest.service.impl;

import com.kgm.nextnest.model.FeaturedPayment;
import com.kgm.nextnest.repository.FeaturedPaymentRepository;
import com.kgm.nextnest.response.FeaturedPaymentResponse;
import com.kgm.nextnest.service.FeaturedPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeaturedPaymentServiceImpl
        implements FeaturedPaymentService {

    private final FeaturedPaymentRepository featuredPaymentRepository;

    @Override
    public List<FeaturedPaymentResponse>
    getOwnerFeaturedHistory(Long ownerId) {

        return featuredPaymentRepository
                .findByOwner_IdOrderByPaidAtDesc(ownerId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private FeaturedPaymentResponse mapToResponse(
            FeaturedPayment payment
    ) {

        return FeaturedPaymentResponse.builder()

                .id(payment.getId())

                .apartmentId(
                        payment.getApartment().getId()
                )

                .apartmentTitle(
                        payment.getApartment().getTitle()
                )

                .plan(
                        payment.getPlan()
                )

                .amount(
                        payment.getAmount()
                )

                .paymentMethod(
                        payment.getPaymentMethod()
                )

                .transactionId(
                        payment.getTransactionId()
                )

                .paid(
                        payment.getPaid()
                )

                .paidAt(
                        payment.getPaidAt()
                )

                .build();
    }
}