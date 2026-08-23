package com.ticketera.domain.valueobject;

import com.ticketera.domain.exception.InvalidOrderException;

public record Money(double value) {
    public Money {
        if (value <= 0) {
            throw new InvalidOrderException("Price must be positive");
        }
    }    
}
