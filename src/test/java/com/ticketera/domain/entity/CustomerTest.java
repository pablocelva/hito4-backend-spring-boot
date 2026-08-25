package com.ticketera.domain.entity;

import com.ticketera.domain.valueobject.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Customer")
class CustomerTest {

    @Test
    @DisplayName("Creates customer with valid data")
    void createsCustomerWithValidData() {
        Email email = new Email("pablo@example.com");
        Customer customer = new Customer("CUS-001", "Pablo", email);
        assertEquals("CUS-001", customer.getId());
        assertEquals("Pablo", customer.getName());
        assertEquals(email, customer.getEmail());
    }

    @Test
    @DisplayName("Throws when id is null")
    void throwsWhenIdIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> new Customer(null, "Pablo", new Email("pablo@example.com")));
        assertEquals("Customer ID cannot be blank", ex.getMessage());
    }

    @Test
    @DisplayName("Throws when id is blank")
    void throwsWhenIdIsBlank() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> new Customer("", "Pablo", new Email("pablo@example.com")));
        assertEquals("Customer ID cannot be blank", ex.getMessage());
    }

    @Test
    @DisplayName("Throws when name is null")
    void throwsWhenNameIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> new Customer("CUS-001", null, new Email("pablo@example.com")));
        assertEquals("Customer name cannot be blank", ex.getMessage());
    }

    @Test
    @DisplayName("Throws when name is blank")
    void throwsWhenNameIsBlank() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> new Customer("CUS-001", "  ", new Email("pablo@example.com")));
        assertEquals("Customer name cannot be blank", ex.getMessage());
    }
}
