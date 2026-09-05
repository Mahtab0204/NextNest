package com.kgm.nextnest.scheduler;

import com.kgm.nextnest.model.Message;
import com.kgm.nextnest.repository.MessageRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageArchiveScheduler {

    private final MessageRepository messageRepository;

    @Scheduled(cron = "0 0 2 * * *")
    public void archiveOldMessages() {

        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(90);

        List<Message> oldMessages = messageRepository.findByArchivedFalseAndSentAtBefore(cutoffDate);

        oldMessages.forEach(message -> message.setArchived(true));

        messageRepository.saveAll(oldMessages);

        System.out.println(
                "Archived "
                        + oldMessages.size()
                        + " messages."

        );
    }
}