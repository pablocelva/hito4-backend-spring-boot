package com.ticketera.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EventId Value Object")
public class EventIdTest {
    @Test
    @DisplayName("Should create EventId and trim value")
    public void shouldCreateAndTrim() {
        EventId id = new EventId("  EVT-001  ");
        assertEquals("EVT-001", id.value());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when id is null")
    public void shouldThrowWhenNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new EventId(null));
        assertEquals("Event ID cannot be blank", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when id is blank")
    public void shouldThrowWhenBlank() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new EventId("   "));
        assertEquals("Event ID cannot be blank", ex.getMessage());
    }
}
