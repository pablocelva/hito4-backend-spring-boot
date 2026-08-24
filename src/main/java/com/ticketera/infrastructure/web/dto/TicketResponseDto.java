package com.ticketera.infrastructure.web.dto;

import com.ticketera.domain.entity.Ticket;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Entrada vendida")
public record TicketResponseDto(
    @Schema(description = "Identificador de la entrada", example = "550e8400-e29b-41d4-a716-446655440000")
    String id,
    @Schema(description = "Identificador del evento", example = "evt-jazz-001")
    String eventId,
    @Schema(description = "Nombre del cliente", example = "Juan Perez")
    String customerName,
    @Schema(description = "Email del cliente", example = "customer@email.com")
    String customerEmail
) {

    public static TicketResponseDto fromDomain(Ticket ticket) {
        return new TicketResponseDto(
            ticket.getId().value(),
            ticket.getEventId().value(),
            ticket.getCustomerName(),
            ticket.getCustomerEmail());
    }
}
