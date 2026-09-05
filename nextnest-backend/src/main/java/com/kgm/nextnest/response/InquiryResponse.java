package com.kgm.nextnest.response;

import com.kgm.nextnest.model.InquiryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
        description = "Inquiry Response Information"
)
public class InquiryResponse {

    private Long id;

    private Long apartmentId;

    private String apartmentTitle;

    private String customerName;

    private String customerEmail;

    private String message;

    private InquiryStatus status;

    private LocalDateTime createdAt;
}