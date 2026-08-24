package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Ticket;
import com.ticketera.domain.repository.TicketRepository;
import com.ticketera.domain.valueobject.EventId;
import com.ticketera.domain.valueobject.TicketId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetEventTicketsUseCaseTest {

    private TicketRepository ticketRepository;
    private GetEventTicketsUseCase useCase;

    @BeforeEach
    void setUp() {
        ticketRepository = mock(TicketRepository.class);
        useCase = new GetEventTicketsUseCase(ticketRepository);
    }

    @Test
    @DisplayName("Returns tickets for an event")
    void returnsTicketsForEvent() {
        List<Ticket> expected = List.of(
            new Ticket(new TicketId("t-1"), new EventId("evt-1"), "Juan", "juan@email.com"),
            new Ticket(new TicketId("t-2"), new EventId("evt-1"), "Ana", "ana@email.com"));
        when(ticketRepository.findByEventId(new EventId("evt-1"))).thenReturn(expected);

        List<Ticket> result = useCase.execute("evt-1");

        assertEquals(2, result.size());
        assertEquals("Juan", result.get(0).getCustomerName());
        verify(ticketRepository).findByEventId(new EventId("evt-1"));
    }

    @Test
    @DisplayName("Returns empty list when no tickets")
    void returnsEmptyListWhenNoTickets() {
        when(ticketRepository.findByEventId(any())).thenReturn(List.of());

        List<Ticket> result = useCase.execute("evt-1");

        assertTrue(result.isEmpty());
    }
}
