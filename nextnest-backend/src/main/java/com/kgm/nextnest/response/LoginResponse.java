package com.kgm.nextnest.response;


import com.kgm.nextnest.model.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        description = "Login Response Information"
)
public class LoginResponse {

    private Long userId;

    private String fullName;

    private String email;

    private RoleType role;

    private String accessToken;

    private String tokenType = "Bearer";

}