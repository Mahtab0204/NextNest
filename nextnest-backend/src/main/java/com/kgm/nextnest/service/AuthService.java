package com.kgm.nextnest.service;


import com.kgm.nextnest.dto.*;
import com.kgm.nextnest.response.JwtAuthResponse;

public interface AuthService {

    JwtAuthResponse registerCustomer(RegisterRequest request);

    JwtAuthResponse registerOwner(RegisterRequest request);

    JwtAuthResponse login(LoginRequest request);

    void verifyEmail(VerifyEmailRequest request);

    void resendOtp (String email);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

}