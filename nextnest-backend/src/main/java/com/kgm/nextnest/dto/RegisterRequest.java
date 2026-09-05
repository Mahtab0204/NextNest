package com.kgm.nextnest.dto;


import com.kgm.nextnest.model.Gender;
import com.kgm.nextnest.model.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "Register Request Information"
)
public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @Pattern(
            regexp = "^01[3-9]\\d{8}$",
            message = "Invalid Bangladeshi phone number"
    )
    private String phone;

    private Gender gender;

    private RoleType role;

}