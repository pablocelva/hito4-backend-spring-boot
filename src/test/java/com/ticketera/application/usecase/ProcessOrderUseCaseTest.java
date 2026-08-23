package com.ticketera.application.usecase;

import com.ticketera.application.port.MessageNotifier;
import com.ticketera.domain.entity.Event;
import com.ticketera.domain.exception.EventNotFoundException;
import com.ticketera.domain.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProcessOrderUseCaseTest {

    private EventRepository repository;
    private MessageNotifier notifier;
    private ProcessOrderUseCase useCase;
    private Event event;

    @BeforeEach
    void setUp() {
        repository = mock(EventRepository.class);
        notifier = mock(MessageNotifier.class);
        useCase = new ProcessOrderUseCase(repository, notifier);
        event = Event.reconstitute(
            new com.ticketera.domain.valueobject.EventId("evt-001"),
            "Jazz Night", "Gran Teatro", 500, 500);
        when(repository.findById(new com.ticketera.domain.valueobject.EventId("evt-001")))
            .thenReturn(Optional.of(event));
    }

    @Test
    @DisplayName("Throws when event id is null")
    void throwsWhenEventIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(null, 2));
    }

    @Test
    @DisplayName("Throws when event id is empty")
    void throwsWhenEventIdIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute("", 2));
    }

    @Test
    @DisplayName("Throws when quantity is zero")
    void throwsWhenQuantityIsZero() {
        assertThrows(com.ticketera.domain.exception.InvalidOrderException.class,
            () -> useCase.execute("evt-001", 0));
    }

    @Test
    @DisplayName("Throws EventNotFoundException when event does not exist")
    void throwsEventNotFoundWhenEventDoesNotExist() {
        when(repository.findById(any())).thenReturn(Optional.empty());
        assertThrows(EventNotFoundException.class, () -> useCase.execute("missing", 2));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Reserves tickets, persists and returns confirmation")
    void reservesTicketsPersistsAndReturnsConfirmation() {
        OrderResult result = useCase.execute("evt-001", 2);

        assertEquals("evt-001", result.eventId());
        assertEquals("Jazz Night", result.eventName());
        assertEquals(2, result.ticketsPurchased());
        assertEquals(498, result.remainingTickets());
        verify(repository).save(event);
        verify(notifier).send(eq("admin@ticketera.com"), contains("Jazz Night"));
    }
}