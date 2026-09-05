package com.kgm.nextnest.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "Conversation Response Information"
)
public class ConversationResponse {

    private Long inquiryId;

    private Long apartmentId;

    private String apartmentTitle;

    private String otherUserName;

    private String lastMessage;

    private LocalDateTime lastMessageTime;

    private Long unreadCount;
}
