package com.ticketera.infrastructure.web.dto;

import com.ticketera.domain.entity.City;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ciudad registrada")
public record CityResponseDto(
    @Schema(description = "Codigo", example = "LIM") String id,
    @Schema(description = "Nombre", example = "Lima") String name
) {

    public static CityResponseDto fromDomain(City city) {
        return new CityResponseDto(city.getId().value(), city.getName());
    }
}