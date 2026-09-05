package com.kgm.nextnest.controller;

import com.kgm.nextnest.dto.ChangePasswordRequest;
import com.kgm.nextnest.dto.UpdateUserRequest;
import com.kgm.nextnest.response.UserProfileResponse;
import com.kgm.nextnest.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(
        name = "CRUD REST APIs for User Resource"
)
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile() {

        return ResponseEntity.ok(userService.getMyProfile());
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @RequestBody
            UpdateUserRequest request) {

        return ResponseEntity.ok(userService.updateProfile(request));
    }

    @PostMapping(
            value = "/me/profile-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<String> uploadProfileImage(
            @RequestParam("image")
            MultipartFile image) {

        userService.uploadProfileImage(image);

        return ResponseEntity.ok("Profile image uploaded successfully");
    }

    @PutMapping("/me/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody
            ChangePasswordRequest request) {

        userService.changePassword(request);

        return ResponseEntity.ok("Password changed successfully");
    }
}