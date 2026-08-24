package com.ticketera.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

@Schema(description = "Peticion de ciudad")
public record CityRequestDto(

    @Schema(description = "Codigo de ciudad (solo en creacion)", example = "LIM")
    String id,

    @Schema(description = "Nombre de la ciudad", example = "Lima")
    @NotBlank(message = "City name is required")
    String name
) {
}