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

    @Test
    @DisplayName("Equals returns true for same value")
    void equalsReturnsTrueForSameValue() {
        CityId a = new CityId(1L);
        CityId b = new CityId(1L);
        assertEquals(a, b);
    }

    @Test
    @DisplayName("Equals returns false for different value")
    void equalsReturnsFalseForDifferentValue() {
        CityId a = new CityId(1L);
        CityId b = new CityId(2L);
        assertNotEquals(a, b);
    }

    @Test
    @DisplayName("Equals returns false for different type")
    void equalsReturnsFalseForDifferentType() {
        CityId a = new CityId(1L);
        assertNotEquals(a, "not a CityId");
    }

    @Test
    @DisplayName("Equals returns true for same reference")
    void equalsReturnsTrueForSameReference() {
        CityId a = new CityId(1L);
        assertEquals(a, a);
    }

    @Test
    @DisplayName("HashCode is consistent for same value")
    void hashCodeIsConsistent() {
        CityId a = new CityId(1L);
        CityId b = new CityId(1L);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
