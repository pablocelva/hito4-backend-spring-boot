package com.ticketera.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

@Schema(description = "Peticion de actualizacion de ciudad. El campo 'code' es inmutable y no se puede modificar despues de la creacion.")
public record UpdateCityRequest(

    @Schema(description = "Nuevo nombre de la ciudad", example = "Lima Metropolitana")
    @NotBlank(message = "City name is required")
    String name
) {
}
