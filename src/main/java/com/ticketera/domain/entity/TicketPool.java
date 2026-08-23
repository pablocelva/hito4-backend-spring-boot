package com.ticketera.domain.entity;

import com.ticketera.domain.exception.SoldOutException;
import com.ticketera.domain.valueobject.TicketQuantity;

public class TicketPool {
    private int available;

    public TicketPool(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.available = capacity;
    }

    public void reserve(TicketQuantity quantity) {
        if (quantity.value() > available) {
            throw new SoldOutException("Not enough tickets available");
        }
        available -= quantity.value();
    }

    public boolean hasAvailability() {
        return available > 0;
    }

    public int getAvailable() {
        return this.available;
    }
}
