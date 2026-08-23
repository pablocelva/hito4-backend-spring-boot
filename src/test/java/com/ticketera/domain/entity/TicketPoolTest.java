package com.ticketera.domain.entity;

import com.ticketera.domain.exception.InvalidOrderException;
import com.ticketera.domain.exception.SoldOutException;
import com.ticketera.domain.valueobject.TicketQuantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Ticket Pool")
public class TicketPoolTest {
    @Test
    @DisplayName("Should throw IllegalArgumentException when capacity is not positive")
    public void shouldThrowWhenCapacityIsNotPositive() {
        assertThrows(IllegalArgumentException.class, () -> new TicketPool(0));
        assertThrows(IllegalArgumentException.class, () -> new TicketPool(-5));
    }

    @Test
    @DisplayName("Should throw InvalidOrderException when quantity is not positive")
    public void shouldThrowInvalidOrderWhenQuantityIsNotPositive() {
        TicketPool pool = new TicketPool(10);
        assertThrows(InvalidOrderException.class, () -> pool.reserve(new TicketQuantity(0)));
    }

    @Test
    @DisplayName("Should throw SoldOutException when not enough tickets")
    public void shouldThrowSoldOutWhenNotEnoughTickets() {
        TicketPool pool = new TicketPool(5);
        assertThrows(SoldOutException.class, () -> pool.reserve(new TicketQuantity(10)));
    }

    @Test
    @DisplayName("Should reserve tickets successfully when available")
    public void shouldReserveTicketsSuccessfully() {
        TicketPool pool = new TicketPool(10);
        pool.reserve(new TicketQuantity(3));
        assertEquals(7, pool.getAvailable());
        assertTrue(pool.hasAvailability());
    }

    @Test
    @DisplayName("Should not have availability when pool is empty")
    public void shouldNotHaveAvailabilityWhenEmpty() {
        TicketPool pool = new TicketPool(1);
        pool.reserve(new TicketQuantity(1));
        assertFalse(pool.hasAvailability());
    }

    @Test
    @DisplayName("Reconstitutes pool preserving available tickets")
    void reconstitutesPoolPreservingAvailableTickets() {
        TicketPool pool = new TicketPool(100, 30);

        assertEquals(30, pool.getAvailable());
        assertTrue(pool.hasAvailability());
    }

    @Test
    @DisplayName("Rejects available greater than capacity on reconstitution")
    void rejectsAvailableGreaterThanCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new TicketPool(100, 150));
    }

    @Test
    @DisplayName("Rejects negative available on reconstitution")
    void rejectsNegativeAvailable() {
        assertThrows(IllegalArgumentException.class, () -> new TicketPool(100, -1));
    }

    @Test
    @DisplayName("Rejects non-positive capacity on reconstitution")
    void rejectsNonPositiveCapacityOnReconstitution() {
        assertThrows(IllegalArgumentException.class, () -> new TicketPool(0, 5));
        assertThrows(IllegalArgumentException.class, () -> new TicketPool(-10, 5));
    }
}