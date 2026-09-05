package com.kgm.nextnest.service;

import com.kgm.nextnest.dto.ChangePasswordRequest;
import com.kgm.nextnest.dto.UpdateUserRequest;
import com.kgm.nextnest.response.UserProfileResponse;
import com.kgm.nextnest.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    UserResponse activateUser(Long id);

    UserResponse deactivateUser(Long id);

    void deleteUser(Long id);

    UserProfileResponse getMyProfile();

    UserProfileResponse updateProfile(
            UpdateUserRequest request
    );

    void uploadProfileImage(
            MultipartFile image
    );

    void changePassword(ChangePasswordRequest request);
}
