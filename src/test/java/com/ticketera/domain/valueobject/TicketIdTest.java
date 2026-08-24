package com.ticketera.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TicketId Value Object")
class TicketIdTest {

    @Test
    @DisplayName("Creates ticket id with valid value")
    void createsTicketIdWithValidValue() {
        TicketId id = new TicketId("t-001");
        assertEquals("t-001", id.value());
    }

    @Test
    @DisplayName("Trims whitespace")
    void trimsWhitespace() {
        TicketId id = new TicketId("  t-001  ");
        assertEquals("t-001", id.value());
    }

    @Test
    @DisplayName("Throws when value is null")
    void throwsWhenValueIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new TicketId(null));
        assertEquals("Ticket ID cannot be blank", ex.getMessage());
    }

    @Test
    @DisplayName("Throws when value is blank")
    void throwsWhenValueIsBlank() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new TicketId("   "));
        assertEquals("Ticket ID cannot be blank", ex.getMessage());
    }
}
