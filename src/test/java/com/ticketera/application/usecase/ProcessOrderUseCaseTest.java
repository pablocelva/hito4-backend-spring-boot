package com.ticketera.application.usecase;

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
    private com.ticketera.application.port.MessageNotifier notifier;
    private ProcessOrderUseCase useCase;
    private Event event;

    @BeforeEach
    void setUp() {
        eventRepository = mock(EventRepository.class);
        ticketRepository = mock(TicketRepository.class);
        notifier = mock(com.ticketera.application.port.MessageNotifier.class);
        useCase = new ProcessOrderUseCase(eventRepository, ticketRepository, notifier);
        event = Event.reconstitute(
            1L, new com.ticketera.domain.valueobject.EventId("evt-001"),
            "Jazz Night", "Gran Teatro", 500, 500);
        when(eventRepository.findById(1L))
            .thenReturn(Optional.of(event));
    }

    @Test
    @DisplayName("Throws EventNotFoundException when event id is null")
    void throwsWhenEventIdIsNull() {
        when(eventRepository.findById(null)).thenReturn(Optional.empty());
        EventNotFoundException ex = assertThrows(EventNotFoundException.class,
            () -> useCase.execute(null, 2));
        assertTrue(ex.getMessage().contains("Event not found"));
    }

    @Test
    @DisplayName("Throws when quantity is zero")
    void throwsWhenQuantityIsZero() {
        com.ticketera.domain.exception.InvalidOrderException ex = assertThrows(
            com.ticketera.domain.exception.InvalidOrderException.class,
            () -> useCase.execute(1L, 0));
        assertEquals("Quantity must be positive", ex.getMessage());
    }

    @Test
    @DisplayName("Throws when quantity is negative")
    void throwsWhenQuantityIsNegative() {
        com.ticketera.domain.exception.InvalidOrderException ex = assertThrows(
            com.ticketera.domain.exception.InvalidOrderException.class,
            () -> useCase.execute(1L, -1));
        assertEquals("Quantity must be positive", ex.getMessage());
    }

    @Test
    @DisplayName("Throws EventNotFoundException when event does not exist")
    void throwsEventNotFoundWhenEventDoesNotExist() {
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());
        EventNotFoundException ex = assertThrows(EventNotFoundException.class,
            () -> useCase.execute(999L, 2));
        assertEquals("Event not found: 999", ex.getMessage());
        verify(eventRepository, never()).save(any());
    }

    @Test
    @DisplayName("Reserves tickets, persists and returns confirmation")
    void reservesTicketsPersistsAndReturnsConfirmation() {
        OrderResult result = useCase.execute(1L, 2);

        assertEquals("evt-001", result.eventId());
        assertEquals("Jazz Night", result.eventName());
        assertEquals(2, result.ticketsPurchased());
        assertEquals(498, result.remainingTickets());
        verify(eventRepository).save(event);
        verify(ticketRepository, times(2)).save(any());
        verify(notifier).send(eq("admin@ticketera.com"), contains("Jazz Night"));
    }

    @Test
    @DisplayName("Uses anonymous when customerName and customerEmail are null")
    void usesAnonymousWhenCustomerInfoIsNull() {
        OrderResult result = useCase.execute(1L, 1, null, null);

        assertEquals("evt-001", result.eventId());
        verify(ticketRepository).save(argThat(ticket ->
            ticket.getCustomerName().equals("anonymous")
                && ticket.getCustomerEmail().equals("")));
    }

    @Test
    @DisplayName("Uses provided customer name and email when supplied")
    void usesProvidedCustomerInfo() {
        OrderResult result = useCase.execute(1L, 1, "Pablo", "pablo@test.com");

        assertEquals("evt-001", result.eventId());
        verify(ticketRepository).save(argThat(ticket ->
            ticket.getCustomerName().equals("Pablo")
                && ticket.getCustomerEmail().equals("pablo@test.com")));
    }
}
