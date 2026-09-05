package com.kgm.nextnest.dto;

import com.kgm.nextnest.model.InquiryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        description = "Update Inquiry Status Request Information"
)
public class UpdateInquiryStatusRequest {

    private InquiryStatus status;
}