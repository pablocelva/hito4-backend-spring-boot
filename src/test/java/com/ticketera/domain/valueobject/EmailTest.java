package com.ticketera.domain.valueobject;

import com.ticketera.domain.exception.InvalidEmailException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Email Value Object")
public class EmailTest {
    @Test
    @DisplayName("Should create email and normalize to lowercase trimmed")
    public void shouldCreateEmailAndNormalize() {
        Email email = new Email("  USER@Example.COM  ");
        assertEquals("user@example.com", email.value());
    }

    @Test
    @DisplayName("Should throw InvalidEmailException when email is null")
    public void shouldThrowWhenEmailIsNull() {
        assertThrows(InvalidEmailException.class, () -> new Email(null));
    }

    @Test
    @DisplayName("Should throw InvalidEmailException when email is blank")
    public void shouldThrowWhenEmailIsBlank() {
        assertThrows(InvalidEmailException.class, () -> new Email("   "));
    }

    @Test
    @DisplayName("Should throw InvalidEmailException when email has no @")
    public void shouldThrowWhenEmailHasNoAtSign() {
        assertThrows(InvalidEmailException.class, () -> new Email("juan-sin-arroba"));
    }

    @Test
    @DisplayName("Should throw InvalidEmailException when email has no domain")
    public void shouldThrowWhenEmailHasNoDomain() {
        assertThrows(InvalidEmailException.class, () -> new Email("user@"));
    }
}