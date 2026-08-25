package com.ticketera.infrastructure.web.dto;

import com.ticketera.domain.entity.City;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ciudad registrada")
public record CityResponseDto(
    @Schema(description = "ID de la ciudad", example = "1") Long id,
    @Schema(description = "Codigo", example = "LIM") String code,
    @Schema(description = "Nombre", example = "Lima") String name
) {

    public static CityResponseDto fromDomain(City city) {
        return new CityResponseDto(
            city.getId() != null ? city.getId().value() : null,
            city.getCode(),
            city.getName());
    }
}
