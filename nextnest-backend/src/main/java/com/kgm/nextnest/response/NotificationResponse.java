package com.kgm.nextnest.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        description = "Notification Response Information"
)
public class NotificationResponse {

    private Long id;

    private String title;

    private String message;

    private String targetUrl;

    private Boolean isRead;

    private LocalDateTime createdAt;
}