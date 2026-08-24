package com.ticketera.domain.entity;

import com.ticketera.domain.exception.InvalidOrderException;
import com.ticketera.domain.exception.SoldOutException;
import com.ticketera.domain.valueobject.EventId;
import com.ticketera.domain.valueobject.TicketQuantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Event")
public class EventTest {
    private Event newEvent() {
        return new Event(new EventId("EVT-001"), "Jazz Night", "Jazz Club", 500);
    }

    @Test
    @DisplayName("Should initialize event with correct values")
    public void shouldInitializeEventWithCorrectValues() {
        Event event = newEvent();
        assertEquals(new EventId("EVT-001"), event.getId());
        assertEquals("Jazz Night", event.getName());
        assertEquals("Jazz Club", event.getVenue());
        assertEquals(500, event.getCapacity());
        assertEquals(0, event.getTicketSold());
    }

    @Test
    @DisplayName("Should return true when tickets are available")
    public void shouldReturnTrueWhenTicketsAreAvailable() {
        assertTrue(newEvent().hasAvailability());
    }

    @Test
    @DisplayName("Should return false when event is sold out")
    public void shouldReturnFalseWhenEventIsSoldOut() {
        Event event = new Event(new EventId("EVT-002"), "Full House", "Arena", 1);
        event.reserveTickets(new TicketQuantity(1));
        assertFalse(event.hasAvailability());
    }

    @Test
    @DisplayName("Should calculate available tickets correctly")
    public void shouldCalculateAvailableTicketsCorrectly() {
        Event event = newEvent();
        event.reserveTickets(new TicketQuantity(3));
        assertEquals(497, event.getAvailableTickets());
        assertEquals(3, event.getTicketSold());
    }

    @Test
    @DisplayName("Should reserve tickets successfully through the aggregate root")
    public void shouldReserveTicketsSuccessfully() {
        Event event = newEvent();
        event.reserveTickets(new TicketQuantity(3));
        assertEquals(497, event.getAvailableTickets());
    }

    @Test
    @DisplayName("Should throw SoldOutException when reserving more than available")
    public void shouldThrowSoldOutWhenNotEnoughTickets() {
        Event event = newEvent();
        SoldOutException ex = assertThrows(SoldOutException.class,
            () -> event.reserveTickets(new TicketQuantity(600)));
        assertEquals("Not enough tickets available", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidOrderException when quantity is not positive")
    public void shouldThrowInvalidOrderWhenQuantityIsNotPositive() {
        Event event = newEvent();
        InvalidOrderException ex = assertThrows(InvalidOrderException.class,
            () -> event.reserveTickets(new TicketQuantity(0)));
        assertEquals("Quantity must be positive", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidOrderException when quantity is negative")
    public void shouldThrowInvalidOrderWhenQuantityIsNegative() {
        Event event = newEvent();
        InvalidOrderException ex = assertThrows(InvalidOrderException.class,
            () -> event.reserveTickets(new TicketQuantity(-1)));
        assertEquals("Quantity must be positive", ex.getMessage());
    }

    @Test
    @DisplayName("Reconstitutes event preserving availability")
    void reconstitutesEventPreservingAvailability() {
        Event event = Event.reconstitute(new EventId("evt-1"), "Jazz Night", "Teatro", 100, 30);

        assertEquals(30, event.getAvailableTickets());
        assertEquals(70, event.getTicketSold());

        event.reserveTickets(new TicketQuantity(10));
        assertEquals(20, event.getAvailableTickets());
    }

    @Test
    @DisplayName("Should update event details successfully")
    void updatesEventDetails() {
        Event event = newEvent();
        event.updateDetails("Rock Night", "Estadio", 1000);
        assertEquals("Rock Night", event.getName());
        assertEquals("Estadio", event.getVenue());
        assertEquals(1000, event.getCapacity());
    }

    @Test
    @DisplayName("Should throw when capacity is less than sold tickets")
    void throwsWhenCapacityLessThanSold() {
        Event event = newEvent();
        event.reserveTickets(new TicketQuantity(100));
        InvalidOrderException ex = assertThrows(InvalidOrderException.class,
            () -> event.updateDetails("Small", "Venue", 50));
        assertTrue(ex.getMessage().contains("cannot be less than sold tickets"));
    }

    @Test
    @DisplayName("Should detect sold tickets")
    void detectsSoldTickets() {
        Event event = newEvent();
        assertFalse(event.hasSoldTickets());
        event.reserveTickets(new TicketQuantity(1));
        assertTrue(event.hasSoldTickets());
    }
}
