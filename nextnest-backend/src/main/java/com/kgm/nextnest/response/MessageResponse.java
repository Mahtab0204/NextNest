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
        description = "Message Response Information"
)
public class MessageResponse {

    private Long id;

    private Long senderId;

    private String senderName;

    private String content;

    private Boolean read;

    private LocalDateTime sentAt;
}