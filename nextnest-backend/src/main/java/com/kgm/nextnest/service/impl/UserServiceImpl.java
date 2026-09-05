package com.kgm.nextnest.service.impl;

import com.kgm.nextnest.dto.ChangePasswordRequest;
import com.kgm.nextnest.dto.UpdateUserRequest;
import com.kgm.nextnest.model.AccountStatus;
import com.kgm.nextnest.model.User;
import com.kgm.nextnest.repository.UserRepository;
import com.kgm.nextnest.response.UserProfileResponse;
import com.kgm.nextnest.response.UserResponse;
import com.kgm.nextnest.security.CustomUserDetails;
import com.kgm.nextnest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToResponse(user);
    }

    @Override
    public UserResponse activateUser(Long id) {

        User user = userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        user.setAccountStatus(AccountStatus.ACTIVE);

        user.setEnabled(true);

        return mapToResponse(userRepository.save(user));
    }

    @Override
    public UserResponse deactivateUser(Long id) {

        User user = userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        user.setAccountStatus(AccountStatus.SUSPENDED);

        user.setEnabled(false);

        return mapToResponse(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
    }


    @Override
    public UserProfileResponse getMyProfile() {

        User user = getCurrentUser();

        return mapToProfileResponse(user);
    }

    @Override
    public UserProfileResponse updateProfile(UpdateUserRequest request) {

        User user = getCurrentUser();
        if(request.getFullName()!=null) user.setFullName(request.getFullName());
        if(request.getPhone()!=null) user.setPhone(request.getPhone());
        if(request.getAddress()!=null) user.setAddress(request.getAddress());
        if(request.getGender()!=null) user.setGender(request.getGender());
        if(request.getDateOfBirth()!=null) user.setDateOfBirth(request.getDateOfBirth());

        return mapToProfileResponse(userRepository.save(user));
    }

    @Override
    public void uploadProfileImage(MultipartFile image) {

        User user = getCurrentUser();

        try {
            String fileName =
                    UUID.randomUUID()
                            + "_"
                            + image.getOriginalFilename();

            Path uploadPath =
                    Paths.get("uploads/profile/");

            Files.createDirectories(uploadPath);

            Files.write(uploadPath.resolve(fileName), image.getBytes());

            user.setProfileImage(fileName);

            userRepository.save(user);

        } catch (Exception e) {

            throw new RuntimeException("Failed to upload image");
        }
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {

        User user = getCurrentUser();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {

            throw new RuntimeException("Current password is incorrect");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {

            throw new RuntimeException("Passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails)
                        authentication.getPrincipal();

        return userRepository
                .findById(
                        userDetails.getId()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        ));
    }

    private UserResponse mapToResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .accountStatus(user.getAccountStatus())
                .verified(user.isVerified())
                .enabled(user.isEnabled())
                .build();
    }

    private UserProfileResponse mapToProfileResponse(User user) {

        return UserProfileResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .gender(user.getGender())
                .dateOfBirth(user.getDateOfBirth())
                .profileImage(user.getProfileImage() == null
                                ? null
                                : "http://localhost:8080/uploads/profile/"
                                  + user.getProfileImage()
                )
                .role(user.getRole())
                .verified(user.isVerified())
                .accountStatus(user.getAccountStatus())
                .build();
    }
}
