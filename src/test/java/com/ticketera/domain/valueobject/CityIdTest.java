package com.ticketera.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CityId Value Object")
class CityIdTest {

    @Test
    @DisplayName("Creates CityId with Long value")
    void createsCityIdWithLongValue() {
        CityId id = new CityId(1L);
        assertEquals(1L, id.value());
    }

    @Test
    @DisplayName("Throws when value is null")
    void throwsWhenValueIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> new CityId(null));
        assertEquals("City id is required", ex.getMessage());
    }
}
