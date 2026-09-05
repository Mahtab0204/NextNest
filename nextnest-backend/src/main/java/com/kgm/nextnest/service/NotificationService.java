package com.kgm.nextnest.service;

import com.kgm.nextnest.model.User;
import com.kgm.nextnest.response.NotificationResponse;

import java.util.List;

public interface NotificationService {

    void createNotification(
            User user,
            String title,
            String message,
            String targetUrl
    );

    List<NotificationResponse> getMyNotifications(String email);

    Long getUnreadCount(String email);

    void markAsRead(Long notificationId, String email);
}