package com.ticketera.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Error estandar de la API")
public record ErrorResponse(
    @Schema(description = "Codigo HTTP", example = "404")
    int code,
    @Schema(description = "Descripcion del error", example = "Evento no encontrado")
    String message,
    @Schema(description = "Timestamp del error", example = "2025-11-15T10:30:00")
    LocalDateTime timestamp
) {

    public static ErrorResponse of(int code, String message) {
        return new ErrorResponse(code, message, LocalDateTime.now());
    }
}
