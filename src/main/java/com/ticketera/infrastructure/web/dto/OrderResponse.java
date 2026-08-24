package com.ticketera.infrastructure.web.dto;

import com.ticketera.application.usecase.OrderResult;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resultado de una orden de compra procesada")
public record OrderResponse(
    @Schema(description = "Identificador del evento", example = "evt-jazz-001")
    String eventId,
    @Schema(description = "Nombre del evento", example = "Jazz Night")
    String eventName,
    @Schema(description = "Entradas adquiridas", example = "2")
    int ticketsPurchased,
    @Schema(description = "Entradas restantes en inventario", example = "498")
    int remainingTickets
) {

    public static OrderResponse fromDomain(OrderResult result) {
        return new OrderResponse(
            result.eventId(),
            result.eventName(),
            result.ticketsPurchased(),
            result.remainingTickets());
    }
}
