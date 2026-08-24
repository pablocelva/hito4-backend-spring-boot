package com.ticketera.application.usecase;

import com.ticketera.application.port.MessageNotifier;
import com.ticketera.domain.entity.Event;
import com.ticketera.domain.exception.EventNotFoundException;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProcessOrderUseCaseTest {

    private EventRepository eventRepository;
    private TicketRepository ticketRepository;
    private MessageNotifier notifier;
    private ProcessOrderUseCase useCase;
    private Event event;

    @BeforeEach
    void setUp() {
        eventRepository = mock(EventRepository.class);
        ticketRepository = mock(TicketRepository.class);
        notifier = mock(MessageNotifier.class);
        useCase = new ProcessOrderUseCase(eventRepository, ticketRepository, notifier);
        event = Event.reconstitute(
            new com.ticketera.domain.valueobject.EventId("evt-001"),
            "Jazz Night", "Gran Teatro", 500, 500);
        when(eventRepository.findById(new com.ticketera.domain.valueobject.EventId("evt-001")))
            .thenReturn(Optional.of(event));
    }

    @Test
    @DisplayName("Throws when event id is null")
    void throwsWhenEventIdIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> useCase.execute(null, 2));
        assertEquals("Event ID cannot be blank", ex.getMessage());
    }

    @Test
    @DisplayName("Throws when event id is empty")
    void throwsWhenEventIdIsEmpty() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> useCase.execute("", 2));
        assertEquals("Event ID cannot be blank", ex.getMessage());
    }

    @Test
    @DisplayName("Throws when quantity is zero")
    void throwsWhenQuantityIsZero() {
        com.ticketera.domain.exception.InvalidOrderException ex = assertThrows(
            com.ticketera.domain.exception.InvalidOrderException.class,
            () -> useCase.execute("evt-001", 0));
        assertEquals("Quantity must be positive", ex.getMessage());
    }

    @Test
    @DisplayName("Throws when quantity is negative")
    void throwsWhenQuantityIsNegative() {
        com.ticketera.domain.exception.InvalidOrderException ex = assertThrows(
            com.ticketera.domain.exception.InvalidOrderException.class,
            () -> useCase.execute("evt-001", -1));
        assertEquals("Quantity must be positive", ex.getMessage());
    }

    @Test
    @DisplayName("Throws EventNotFoundException when event does not exist")
    void throwsEventNotFoundWhenEventDoesNotExist() {
        when(eventRepository.findById(any())).thenReturn(Optional.empty());
        EventNotFoundException ex = assertThrows(EventNotFoundException.class,
            () -> useCase.execute("missing", 2));
        assertEquals("Event not found: missing", ex.getMessage());
        verify(eventRepository, never()).save(any());
    }

    @Test
    @DisplayName("Reserves tickets, persists and returns confirmation")
    void reservesTicketsPersistsAndReturnsConfirmation() {
        OrderResult result = useCase.execute("evt-001", 2);

        assertEquals("evt-001", result.eventId());
        assertEquals("Jazz Night", result.eventName());
        assertEquals(2, result.ticketsPurchased());
        assertEquals(498, result.remainingTickets());
        verify(eventRepository).save(event);
        verify(ticketRepository, times(2)).save(any());
        verify(notifier).send(eq("admin@ticketera.com"), contains("Jazz Night"));
    }
}
