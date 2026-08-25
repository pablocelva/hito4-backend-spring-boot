package com.ticketera.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

@Schema(description = "Peticion de ciudad")
public record CityRequestDto(

    @Schema(description = "Codigo de ciudad", example = "LIM")
    @NotBlank(message = "City code is required")
    String code,

    @Schema(description = "Nombre de la ciudad", example = "Lima")
    @NotBlank(message = "City name is required")
    String name
) {
}
