package com.kgm.nextnest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(
        description = "AI Search Request Information"
)
public record AiSearchRequest(
        Integer bedrooms,
        Integer bathrooms,
        Double maxPrice,
        Double minPrice,
        String furnishing,
        String locationKeyword
) {}