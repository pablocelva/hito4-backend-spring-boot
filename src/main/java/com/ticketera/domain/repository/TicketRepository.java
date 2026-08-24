package com.ticketera.domain.repository;

import com.ticketera.domain.entity.Ticket;
import com.ticketera.domain.valueobject.EventId;

import java.util.List;

public interface TicketRepository {

    List<Ticket> findByEventId(EventId eventId);

    void save(Ticket ticket);
}
