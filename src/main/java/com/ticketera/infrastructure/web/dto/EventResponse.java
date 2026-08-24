package com.ticketera.infrastructure.web.dto;

import com.ticketera.domain.entity.Event;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Evento de la cartelera con su inventario")
public record EventResponse(
    @Schema(description = "Identificador unico", example = "evt-jazz-001") String id,
    @Schema(description = "Nombre del evento", example = "Jazz Night") String name,
    @Schema(description = "Lugar", example = "Gran Teatro Lima") String venue,
    @Schema(description = "Capacidad total", example = "500") int capacity,
    @Schema(description = "Entradas disponibles", example = "498") int availableTickets,
    @Schema(description = "Entradas vendidas", example = "2") int ticketsSold
) {

    public static EventResponse fromDomain(Event event) {
        return new EventResponse(
            event.getId().value(),
            event.getName(),
            event.getVenue(),
            event.getCapacity(),
            event.getAvailableTickets(),
            event.getTicketSold());
    }
}