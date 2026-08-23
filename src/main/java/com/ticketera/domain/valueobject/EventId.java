package com.ticketera.domain.valueobject;

public record EventId(String value) {
    public EventId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Event ID cannot be blank");
        }
        value = value.trim();
    }
}
