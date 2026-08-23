package com.ticketera.application.usecase;

import com.ticketera.application.port.MessageNotifier;
import com.ticketera.domain.exception.InvalidEmailException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("Send Booking Confirmation Use Case")
public class SendBookingConfirmationUseCaseTest {
    @Test
    @DisplayName("Should fail when email is null")
    public void shouldFailWhenEmailIsNull() {
        MessageNotifier notifierMock = mock(MessageNotifier.class);
        SendBookingConfirmationUseCase useCase = new SendBookingConfirmationUseCase(notifierMock);

        assertThrows(InvalidEmailException.class, () -> useCase.execute(null, "Jazz Night"));
    }

    @Test
    @DisplayName("Should fail when email is empty")
    public void shouldFailWhenEmailIsEmpty() {
        MessageNotifier notifierMock = mock(MessageNotifier.class);
        SendBookingConfirmationUseCase useCase = new SendBookingConfirmationUseCase(notifierMock);

        assertThrows(InvalidEmailException.class, () -> useCase.execute("", "Jazz Night"));
    }

    @Test
    @DisplayName("Should send confirmation successfully")
    public void shouldSendConfirmationSuccessfully() {
        MessageNotifier notifierMock = mock(MessageNotifier.class);
        SendBookingConfirmationUseCase useCase = new SendBookingConfirmationUseCase(notifierMock);

        useCase.execute("customer@email.com", "Jazz Night");

        verify(notifierMock, times(1)).send("customer@email.com", "Booking confirmed for: Jazz Night");
    }
}