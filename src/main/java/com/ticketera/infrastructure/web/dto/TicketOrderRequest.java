package com.ticketera.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Schema(description = "Peticion de compra de entradas")
public record TicketOrderRequest(

    @Schema(description = "Identificador del evento", example = "evt-jazz-001")
    @NotBlank(message = "Event id is required")
    String eventId,

    @Schema(description = "Cantidad de entradas a comprar", example = "2")
    @Positive(message = "Quantity must be positive")
    int quantity,

    @Schema(description = "Nombre del cliente", example = "Juan Perez")
    String customerName,

    @Schema(description = "Email opcional para confirmacion", example = "customer@email.com")
    @Email(message = "Customer email must be valid")
    String customerEmail
) {
}
