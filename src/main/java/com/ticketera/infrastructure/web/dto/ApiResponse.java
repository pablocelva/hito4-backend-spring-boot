package com.ticketera.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Respuesta generica de la API")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse(
    @Schema(description = "Codigo HTTP", example = "200")
    int status,
    @Schema(description = "Mensaje descriptivo", example = "Operacion exitosa")
    String message,
    @Schema(description = "Nombre del recurso", example = "evt-jazz-001")
    String name,
    @Schema(description = "Timestamp de la respuesta")
    LocalDateTime timestamp
) {
    public static ApiResponse ok(String message, String name) {
        return new ApiResponse(200, message, name, LocalDateTime.now());
    }

    public static ApiResponse ok(String message) {
        return new ApiResponse(200, message, null, LocalDateTime.now());
    }

    public static ApiResponse error(int status, String message) {
        return new ApiResponse(status, message, null, LocalDateTime.now());
    }
}