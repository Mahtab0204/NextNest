package com.kgm.nextnest.repository;

import com.kgm.nextnest.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification>
    findByUserIdOrderByCreatedAtDesc(
            Long userId
    );

    Long countByUserIdAndIsReadFalse(
            Long userId
    );
}