package com.ticketera.domain.valueobject;

public class CityId {

    private final String value;

    public CityId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("City id is required");
        }
        this.value = value.trim();
    }

    public String value() {
        return value;
    }
}