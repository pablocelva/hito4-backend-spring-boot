package com.ticketera.domain.valueobject;

import com.ticketera.domain.exception.InvalidEmailException;

public record Email(String value) {
    public Email {
        String cleanValue = value == null ? "" : value.trim().toLowerCase();
        if (cleanValue.isBlank()
                || !cleanValue.matches("^[a-z0-9]+([._%+-][a-z0-9]+)*@[a-z0-9.-]+\\.[a-z]{2,}$")) {
            throw new InvalidEmailException("Invalid email: " + value);
        }
        value = cleanValue;
    }
}
