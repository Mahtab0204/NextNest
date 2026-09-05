package com.kgm.nextnest.response;

import com.kgm.nextnest.model.AccountStatus;
import com.kgm.nextnest.model.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        description = "User Response Information"
)
public class UserResponse {

    private Long id;

    private String fullName;

    private String email;

    private String phone;

    private RoleType role;

    private AccountStatus accountStatus;

    private Boolean verified;

    private Boolean enabled;
}