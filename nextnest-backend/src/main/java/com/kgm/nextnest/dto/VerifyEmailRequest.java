package com.kgm.nextnest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        description = "Verify Email Request Information"
)
public class VerifyEmailRequest {

    private String email;

    private String otp;
}