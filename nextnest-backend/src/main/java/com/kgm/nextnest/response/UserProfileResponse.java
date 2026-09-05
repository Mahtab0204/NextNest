package com.kgm.nextnest.response;

import com.kgm.nextnest.model.AccountStatus;
import com.kgm.nextnest.model.Gender;
import com.kgm.nextnest.model.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "User Profile Response Information"
)
public class UserProfileResponse {

    private Long id;

    private String fullName;

    private String email;

    private String phone;

    private String address;

    private Gender gender;

    private LocalDate dateOfBirth;

    private String profileImage;

    private RoleType role;

    private boolean verified;

    private AccountStatus accountStatus;
}