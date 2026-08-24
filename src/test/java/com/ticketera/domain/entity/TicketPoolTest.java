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
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
            () -> new TicketPool(0));
        assertEquals("Capacity must be positive", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
            () -> new TicketPool(-5));
        assertEquals("Capacity must be positive", ex2.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidOrderException when quantity is not positive")
    public void shouldThrowInvalidOrderWhenQuantityIsNotPositive() {
        TicketPool pool = new TicketPool(10);
        InvalidOrderException ex = assertThrows(InvalidOrderException.class,
            () -> pool.reserve(new TicketQuantity(0)));
        assertEquals("Quantity must be positive", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw SoldOutException when not enough tickets")
    public void shouldThrowSoldOutWhenNotEnoughTickets() {
        TicketPool pool = new TicketPool(5);
        SoldOutException ex = assertThrows(SoldOutException.class,
            () -> pool.reserve(new TicketQuantity(10)));
        assertEquals("Not enough tickets available", ex.getMessage());
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
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> new TicketPool(100, 150));
        assertEquals("Available must be between 0 and capacity", ex.getMessage());
    }

    @Test
    @DisplayName("Rejects negative available on reconstitution")
    void rejectsNegativeAvailable() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> new TicketPool(100, -1));
        assertEquals("Available must be between 0 and capacity", ex.getMessage());
    }

    @Test
    @DisplayName("Rejects non-positive capacity on reconstitution")
    void rejectsNonPositiveCapacityOnReconstitution() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
            () -> new TicketPool(0, 5));
        assertEquals("Capacity must be positive", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
            () -> new TicketPool(-10, 5));
        assertEquals("Capacity must be positive", ex2.getMessage());
    }
}
