package com.kgm.nextnest.controller;

import com.kgm.nextnest.response.UserResponse;
import com.kgm.nextnest.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Tag(
        name = "CRUD REST APIs for Admin User Resource"
)
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>>
    getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse>
    getUserById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                userService.getUserById(id)
        );
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<UserResponse>
    activateUser(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                userService.activateUser(id)
        );
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<UserResponse>
    deactivateUser(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                userService.deactivateUser(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String>
    deleteUser(
            @PathVariable Long id
    ) {

        userService.deleteUser(id);

        return ResponseEntity.ok(
                "User deleted successfully"
        );
    }
}