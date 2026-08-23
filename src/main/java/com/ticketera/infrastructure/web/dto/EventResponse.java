package com.ticketera.infrastructure.web.dto;

import com.ticketera.domain.entity.Event;

public record EventResponse(
    String id,
    String name,
    String venue,
    int capacity,
    int availableTickets,
    int ticketsSold
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