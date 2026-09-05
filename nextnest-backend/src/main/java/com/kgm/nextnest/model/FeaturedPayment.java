package com.kgm.nextnest.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "featured_payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeaturedPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Enumerated(EnumType.STRING)
    private FeaturedPlan plan;

    private Double amount;

    private String paymentMethod;

    private String transactionId;

    private Boolean paid;

    private LocalDateTime paidAt;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
}