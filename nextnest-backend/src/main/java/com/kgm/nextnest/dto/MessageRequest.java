package com.kgm.nextnest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        description = "Message Request Information"
)
public class MessageRequest {

    private Long inquiryId;

    private String content;
}