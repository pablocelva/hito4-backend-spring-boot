package com.ticketera.domain.valueobject;

import com.ticketera.domain.exception.InvalidOrderException;

public record TicketQuantity(int value) {
    public TicketQuantity {
        if (value <= 0) {
            throw new InvalidOrderException("Quantity must be positive");
        }
    }
}
