package com.kgm.nextnest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        description = "Reset Password Request Information"
)
public class ResetPasswordRequest {

    private String email;

    private String otp;

    private String newPassword;
}