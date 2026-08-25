package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Ticket;
import com.ticketera.domain.repository.TicketRepository;

import java.util.List;

public class GetEventTicketsUseCase {

    private final TicketRepository ticketRepository;

    public GetEventTicketsUseCase(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> execute(Long eventId) {
        return ticketRepository.findByEventId(eventId);
    }
}
