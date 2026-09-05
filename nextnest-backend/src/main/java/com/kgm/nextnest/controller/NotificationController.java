package com.kgm.nextnest.controller;

import com.kgm.nextnest.response.NotificationResponse;
import com.kgm.nextnest.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(
        name = "CRUD REST APIs for Notification Resource"
)
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(notificationService.getMyNotifications(email));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(Authentication authentication) {

        String email = authentication.getName();

        Long count = notificationService.getUnreadCount(email);

        return ResponseEntity.ok(Map.of("count", count)
        );
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<String> markAsRead(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        notificationService.markAsRead(id, email);

        return ResponseEntity.ok("Notification marked as read");
    }
}