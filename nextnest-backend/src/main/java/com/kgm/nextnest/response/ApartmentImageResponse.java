package com.kgm.nextnest.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(
        description = "Apartment Image Response Information"
)
public class ApartmentImageResponse {

    private Long id;

    private String imageUrl;

    private Boolean coverImage;
}