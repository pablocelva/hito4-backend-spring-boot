package com.ticketera.domain.valueobject;

import java.util.Objects;

public class CityId {

    private final Long value;

    public CityId(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("City id is required");
        }
        this.value = value;
    }

    public Long value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CityId cityId)) return false;
        return Objects.equals(value, cityId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
