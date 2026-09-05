package com.kgm.nextnest.repository;

import com.kgm.nextnest.model.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationRepository
        extends JpaRepository<EmailVerification, Long> {

    Optional<EmailVerification>
    findByEmail(String email);

    Optional<EmailVerification>
    findByEmailAndOtp(
            String email,
            String otp
    );

    void deleteByEmail(String email);
}