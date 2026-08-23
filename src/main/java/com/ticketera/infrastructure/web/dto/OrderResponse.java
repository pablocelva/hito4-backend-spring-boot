package com.ticketera.infrastructure.web.dto;

import com.ticketera.application.usecase.OrderResult;

public record OrderResponse(
    String eventId,
    String eventName,
    int ticketsPurchased,
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