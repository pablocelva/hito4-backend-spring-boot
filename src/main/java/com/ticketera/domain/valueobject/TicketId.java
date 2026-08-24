package com.ticketera.domain.valueobject;

public record TicketId(String value) {
    public TicketId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Ticket ID cannot be blank");
        }
        value = value.trim();
    }
}
