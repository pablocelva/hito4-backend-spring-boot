package com.ticketera.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("City")
class CityTest {

    @Test
    @DisplayName("Creates city with valid data")
    void createsCityWithValidData() {
        City city = new City(1L, "LIM", "Lima");
        assertEquals(1L, city.getId().value());
        assertEquals("LIM", city.getCode());
        assertEquals("Lima", city.getName());
    }

    @Test
    @DisplayName("Throws when code is null")
    void throwsWhenCodeIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> new City(1L, null, "Lima"));
        assertEquals("City code is required", ex.getMessage());
    }

    @Test
    @DisplayName("Throws when code is blank")
    void throwsWhenCodeIsBlank() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> new City(1L, "  ", "Lima"));
        assertEquals("City code is required", ex.getMessage());
    }

    @Test
    @DisplayName("Throws when name is null")
    void throwsWhenNameIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> new City(1L, "LIM", null));
        assertEquals("City name is required", ex.getMessage());
    }

    @Test
    @DisplayName("Throws when name is blank")
    void throwsWhenNameIsBlank() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> new City(1L, "LIM", "  "));
        assertEquals("City name is required", ex.getMessage());
    }

    @Test
    @DisplayName("Renames city successfully")
    void renamesCity() {
        City city = new City(1L, "LIM", "Lima");
        city.rename("Lima Metropolitana");
        assertEquals("Lima Metropolitana", city.getName());
    }

    @Test
    @DisplayName("Throws when renaming to null")
    void throwsWhenRenamingToNull() {
        City city = new City(1L, "LIM", "Lima");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> city.rename(null));
        assertEquals("City name is required", ex.getMessage());
    }

    @Test
    @DisplayName("Throws when renaming to blank")
    void throwsWhenRenamingToBlank() {
        City city = new City(1L, "LIM", "Lima");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> city.rename("  "));
        assertEquals("City name is required", ex.getMessage());
    }
}
