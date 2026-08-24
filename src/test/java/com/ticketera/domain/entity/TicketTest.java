package com.ticketera.domain.entity;

import com.ticketera.domain.valueobject.EventId;
import com.ticketera.domain.valueobject.TicketId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Ticket")
class TicketTest {

    @Test
    @DisplayName("Creates ticket with all fields")
    void createsTicketWithAllFields() {
        Ticket ticket = new Ticket(
            new TicketId("t-001"),
            new EventId("evt-001"),
            "Juan Perez",
            "juan@email.com");

        assertEquals("t-001", ticket.getId().value());
        assertEquals("evt-001", ticket.getEventId().value());
        assertEquals("Juan Perez", ticket.getCustomerName());
        assertEquals("juan@email.com", ticket.getCustomerEmail());
    }

    @Test
    @DisplayName("Creates ticket with anonymous customer")
    void createsTicketWithAnonymousCustomer() {
        Ticket ticket = new Ticket(
            new TicketId("t-002"),
            new EventId("evt-001"),
            "anonymous",
            "");

        assertEquals("anonymous", ticket.getCustomerName());
        assertEquals("", ticket.getCustomerEmail());
    }
}
