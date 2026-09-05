package com.kgm.nextnest.controller;

import com.kgm.nextnest.dto.*;
import com.kgm.nextnest.response.JwtAuthResponse;
import com.kgm.nextnest.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(
        name = "CRUD REST APIs for Auth Resource"
)
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/customer")
    public ResponseEntity<JwtAuthResponse> registerCustomer(
            @Valid @RequestBody RegisterRequest request) {

        JwtAuthResponse response = authService.registerCustomer(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/register/owner")
    public ResponseEntity<JwtAuthResponse> registerOwner(
            @Valid @RequestBody RegisterRequest request) {

        JwtAuthResponse response = authService.registerOwner(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        JwtAuthResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(
            @RequestBody VerifyEmailRequest request) {

        authService.verifyEmail(request);

        return ResponseEntity.ok("Email verified successfully");
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(
            @RequestBody ResendOtpRequest request) {

        authService.resendOtp(request.getEmail());

        return ResponseEntity.ok("OTP sent successfully");
    }

    @PostMapping("/forgot-password") public ResponseEntity<String> forgotPassword(
            @RequestBody
            ForgotPasswordRequest request) {

        authService.forgotPassword(request);

        return ResponseEntity.ok("OTP sent successfully");
    }

    @PostMapping("/reset-password") public ResponseEntity<String> resetPassword(
            @RequestBody
            ResetPasswordRequest request) {

        authService.resetPassword(request);

        return ResponseEntity.ok("Password reset successful");
    }

}