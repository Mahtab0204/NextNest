package com.kgm.nextnest.scheduler;

import com.kgm.nextnest.repository.MessageRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class MessageDeleteScheduler {

    private final MessageRepository messageRepository;

    @Scheduled(cron = "0 30 2 * * *")
    public void deleteArchivedMessages() {

        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(180);

        messageRepository.deleteByArchivedTrueAndSentAtBefore(cutoffDate);

        System.out.println("Old archived messages deleted.");
    }
}