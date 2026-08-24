package com.ticketera.domain.valueobject;

import com.ticketera.domain.exception.InvalidOrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TicketQuantity Value Object")
public class TicketQuantityTest {
    @Test
    @DisplayName("Should create valid quantity")
    public void shouldCreateValidQuantity() {
        TicketQuantity qty = new TicketQuantity(2);
        assertEquals(2, qty.value());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10})
    @DisplayName("Should throw InvalidOrderException when quantity is less than or equal to zero")
    public void shouldThrowWhenQuantityIsNotPositive(int invalid) {
        InvalidOrderException ex = assertThrows(InvalidOrderException.class, () -> new TicketQuantity(invalid));
        assertEquals("Quantity must be positive", ex.getMessage());
    }
}
