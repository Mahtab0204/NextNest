package com.kgm.nextnest.service.impl;


import com.kgm.nextnest.dto.*;
import com.kgm.nextnest.exception.EmailAlreadyExistsException;
import com.kgm.nextnest.model.EmailVerification;
import com.kgm.nextnest.model.RoleType;
import com.kgm.nextnest.model.User;
import com.kgm.nextnest.repository.EmailVerificationRepository;
import com.kgm.nextnest.repository.UserRepository;
import com.kgm.nextnest.response.JwtAuthResponse;
import com.kgm.nextnest.security.CustomUserDetails;
import com.kgm.nextnest.security.JwtTokenProvider;
import com.kgm.nextnest.service.AuthService;
import com.kgm.nextnest.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailVerificationRepository emailVerificationRepository;
    private final EmailService emailService;


     // Register Customer
    @Override
    public JwtAuthResponse registerCustomer(RegisterRequest request) {

        return registerUser(request, RoleType.CUSTOMER);

    }


     // Register Owner

    @Override
    public JwtAuthResponse registerOwner(RegisterRequest request) {

        return registerUser(request, RoleType.OWNER);

    }


     // Common Registration Method

    private JwtAuthResponse registerUser(
            RegisterRequest request,
            RoleType role
    ) {

        if (
                userRepository.existsByEmail(
                        request.getEmail()
                )
        ) {

            throw new EmailAlreadyExistsException(
                    "Email already exists."
            );
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .role(role)
                .enabled(true)
                .verified(false)
                .build();

        userRepository.save(user);

        String otp = generateOtp();

        emailVerificationRepository
                .findByEmail(user.getEmail())
                .ifPresent(
                        emailVerificationRepository::delete
                );

        EmailVerification verification =
                EmailVerification.builder()
                        .email(user.getEmail())
                        .otp(otp)
                        .expiryTime(
                                LocalDateTime.now()
                                        .plusMinutes(10)
                        )
                        .build();

        emailVerificationRepository.save(
                verification
        );

        emailService.sendVerificationOtp(
                user.getEmail(),
                otp
        );

        return JwtAuthResponse.builder()
                .accessToken(null)
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }


     // Login

    @Override
    public JwtAuthResponse login(
            LoginRequest request
    ) {

        User user =
                userRepository
                        .findByEmail(
                                request.getEmail()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invalid credentials"
                                )
                        );

        if (!user.isVerified()) {

            throw new RuntimeException(
                    "Please verify your email first."
            );
        }

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        String token =
                jwtTokenProvider.generateToken(
                        authentication
                );

        CustomUserDetails userDetails =
                (CustomUserDetails)
                        authentication.getPrincipal();

        return JwtAuthResponse.builder()
                .accessToken(token)
                .userId(userDetails.getId())
                .fullName(userDetails.getFullName())
                .email(userDetails.getEmail())
                .role(
                        RoleType.valueOf(
                                userDetails.getRole()
                        )
                )
                .build();
    }

    @Override
    public void verifyEmail(
            VerifyEmailRequest request
    ) {

        EmailVerification verification =
                emailVerificationRepository
                        .findByEmailAndOtp(
                                request.getEmail(),
                                request.getOtp()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invalid OTP"
                                )
                        );

        if (
                verification.getExpiryTime()
                        .isBefore(
                                LocalDateTime.now()
                        )
        ) {

            throw new RuntimeException(
                    "OTP expired"
            );
        }

        User user =
                userRepository
                        .findByEmail(
                                request.getEmail()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        user.setVerified(true);

        userRepository.save(user);

        emailVerificationRepository.delete(
                verification
        );
    }

    @Override
    public void resendOtp(String email) {
        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        if (user.isVerified()) {

            throw new RuntimeException(
                    "Email already verified"
            );
        }

        emailVerificationRepository
                .deleteByEmail(email);

        String otp =
                generateOtp();

        EmailVerification verification =
                EmailVerification.builder()
                        .email(email)
                        .otp(otp)
                        .expiryTime(
                                LocalDateTime.now()
                                        .plusMinutes(10)
                        )
                        .build();

        emailVerificationRepository.save(
                verification
        );

        emailService.sendVerificationOtp(
                email,
                otp
        );
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        User user =
                userRepository
                        .findByEmail(
                                request.getEmail()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        String otp =
                generateOtp();

        emailVerificationRepository
                .findByEmail(
                        user.getEmail()
                )
                .ifPresent(
                        emailVerificationRepository::delete
                );

        EmailVerification verification =
                EmailVerification.builder()
                        .email(
                                user.getEmail()
                        )
                        .otp(
                                otp
                        )
                        .expiryTime(
                                LocalDateTime.now()
                                        .plusMinutes(10)
                        )
                        .build();

        emailVerificationRepository.save(
                verification
        );

        emailService.sendVerificationOtp(
                user.getEmail(),
                otp
        );
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        EmailVerification verification =
                emailVerificationRepository
                        .findByEmailAndOtp(
                                request.getEmail(),
                                request.getOtp()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invalid OTP"
                                )
                        );

        if (
                verification.getExpiryTime()
                        .isBefore(
                                LocalDateTime.now()
                        )
        ) {

            throw new RuntimeException(
                    "OTP expired"
            );
        }

        User user =
                userRepository
                        .findByEmail(
                                request.getEmail()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(
                user
        );

        emailVerificationRepository.delete(
                verification
        );

    }

    private String generateOtp() {

        return String.valueOf(
                100000 +
                        new Random().nextInt(900000)
        );
    }

}