package com.ticketera.domain.entity;

import com.ticketera.domain.valueobject.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Customer")
public class CustomerTest {
    @Test
    @DisplayName("Should create customer with valid data")
    public void shouldCreateCustomer() {
        Customer customer = new Customer("CUS-001", "Pablo", new Email("pablo@example.com"));
        assertEquals("CUS-001", customer.getId());
        assertEquals("Pablo", customer.getName());
        assertEquals("pablo@example.com", customer.getEmail().value());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when id is null")
    public void shouldThrowWhenIdIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Customer(null, "Pablo", new Email("pablo@example.com")));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when id is blank")
    public void shouldThrowWhenIdIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> new Customer("", "Pablo", new Email("pablo@example.com")));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when name is null")
    public void shouldThrowWhenNameIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Customer("CUS-001", null, new Email("pablo@example.com")));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when name is blank")
    public void shouldThrowWhenNameIsBlank() {
        assertThrows(IllegalArgumentException.class,
                () -> new Customer("CUS-001", "  ", new Email("pablo@example.com")));
    }
}