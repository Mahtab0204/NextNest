package com.kgm.nextnest.service.impl;

import com.kgm.nextnest.model.Notification;
import com.kgm.nextnest.model.User;
import com.kgm.nextnest.repository.NotificationRepository;
import com.kgm.nextnest.repository.UserRepository;
import com.kgm.nextnest.response.NotificationResponse;
import com.kgm.nextnest.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;



    @Override
    public void createNotification(
            User user,
            String title,
            String message,
            String targetUrl
    ) {

        Notification notification = Notification.builder()
                        .user(user)
                        .title(title)
                        .message(message)
                        .targetUrl(targetUrl)
                        .isRead(false)
                        .build();

        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationResponse> getMyNotifications(String email) {

        User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public Long getUnreadCount(String email) {

        User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        return notificationRepository.countByUserIdAndIsReadFalse(user.getId());
    }

    @Override
    public void markAsRead(Long notificationId, String email) {

        User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = notificationRepository.findById(notificationId)
                        .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getUser().getId().equals(user.getId())) {

            throw new RuntimeException("Access denied");
        }

        notification.setIsRead(true);

        notificationRepository.save(notification);
    }

    private NotificationResponse mapToResponse(Notification notification) {

        return NotificationResponse
                .builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .targetUrl(notification.getTargetUrl())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}