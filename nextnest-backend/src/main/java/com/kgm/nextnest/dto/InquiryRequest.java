package com.kgm.nextnest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "Inquiry Request Information"
)
public class InquiryRequest {

    private Long apartmentId;
    private String message;
}