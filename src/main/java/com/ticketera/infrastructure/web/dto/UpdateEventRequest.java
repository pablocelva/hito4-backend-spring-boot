package com.ticketera.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Schema(description = "Peticion de actualizacion de evento")
public record UpdateEventRequest(

    @Schema(description = "Nombre del evento", example = "Jazz Night Updated")
    @NotBlank(message = "Name is required")
    String name,

    @Schema(description = "Lugar del evento", example = "Gran Teatro Lima")
    @NotBlank(message = "Venue is required")
    String venue,

    @Schema(description = "Capacidad total de entradas", example = "600")
    @Positive(message = "Capacity must be positive")
    int capacity
) {
}
