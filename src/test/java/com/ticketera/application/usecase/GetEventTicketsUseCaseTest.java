package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Ticket;
import com.ticketera.domain.repository.TicketRepository;
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
            new Ticket(new TicketId("t-1"), 1L, "Juan", "juan@email.com"),
            new Ticket(new TicketId("t-2"), 1L, "Ana", "ana@email.com"));
        when(ticketRepository.findByEventId(1L)).thenReturn(expected);

        List<Ticket> result = useCase.execute(1L);

        assertEquals(2, result.size());
        assertEquals("Juan", result.get(0).getCustomerName());
        verify(ticketRepository).findByEventId(1L);
    }

    @Test
    @DisplayName("Returns empty list when no tickets")
    void returnsEmptyListWhenNoTickets() {
        when(ticketRepository.findByEventId(any())).thenReturn(List.of());

        List<Ticket> result = useCase.execute(1L);

        assertTrue(result.isEmpty());
    }
}
