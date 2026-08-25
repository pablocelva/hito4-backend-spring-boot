package com.ticketera.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Schema(description = "Peticion de creacion de evento")
public record CreateEventRequest(

    @Schema(description = "ID de la ciudad", example = "1")
    @Positive(message = "City ID must be positive")
    Long cityId,

    @Schema(description = "Nombre del evento", example = "Jazz Night")
    @NotBlank(message = "Name is required")
    String name,

    @Schema(description = "Lugar del evento", example = "Gran Teatro Lima")
    @NotBlank(message = "Venue is required")
    String venue,

    @Schema(description = "Capacidad total de entradas", example = "500")
    @Positive(message = "Capacity must be positive")
    int capacity
) {
}
