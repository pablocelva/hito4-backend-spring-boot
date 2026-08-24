package com.ticketera.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CityId Value Object")
class CityIdTest {

    @Test
    @DisplayName("Creates CityId and trims value")
    void createsCityIdAndTrims() {
        CityId id = new CityId("  LIM  ");
        assertEquals("LIM", id.value());
    }

    @Test
    @DisplayName("Throws when value is null")
    void throwsWhenValueIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> new CityId(null));
        assertEquals("City id is required", ex.getMessage());
    }

    @Test
    @DisplayName("Throws when value is blank")
    void throwsWhenValueIsBlank() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> new CityId("   "));
        assertEquals("City id is required", ex.getMessage());
    }
}
