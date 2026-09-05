package com.kgm.nextnest.repository;

import com.kgm.nextnest.model.Message;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MessageRepository
        extends JpaRepository<Message, Long> {

    List<Message> findByInquiryIdOrderBySentAtAsc(
            Long inquiryId
    );

    long countByInquiryIdAndIsReadFalse(
            Long inquiryId
    );

    Optional<Message>
    findFirstByInquiryIdOrderBySentAtDesc(
            Long inquiryId
    );

    List<Message> findByInquiryIdAndArchivedFalseOrderBySentAtAsc(Long inquiryId);
    List<Message> findByArchivedFalse();
    List<Message> findByArchivedFalseAndInquiryId(Long inquiryId);
    List<Message> findByArchivedFalseAndSentAtBefore(LocalDateTime date);
    @Transactional
    void deleteByArchivedTrueAndSentAtBefore(
            LocalDateTime date
    );

}