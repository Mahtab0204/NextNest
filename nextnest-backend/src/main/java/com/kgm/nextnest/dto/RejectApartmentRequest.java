package com.kgm.nextnest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        description = "Reject Apartment Request Information"
)
public class RejectApartmentRequest {

    private String reason;
}