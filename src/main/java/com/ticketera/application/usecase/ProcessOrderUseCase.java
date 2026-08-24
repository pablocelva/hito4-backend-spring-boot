package com.ticketera.application.usecase;

import com.ticketera.application.port.MessageNotifier;
import com.ticketera.domain.entity.Event;
import com.ticketera.domain.entity.Ticket;
import com.ticketera.domain.exception.EventNotFoundException;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.repository.TicketRepository;
import com.ticketera.domain.valueobject.EventId;
import com.ticketera.domain.valueobject.TicketId;
import com.ticketera.domain.valueobject.TicketQuantity;

import java.util.UUID;

public class ProcessOrderUseCase {

    private static final String ADMIN_EMAIL = "admin@ticketera.com";

    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final MessageNotifier notifier;

    public ProcessOrderUseCase(EventRepository eventRepository, TicketRepository ticketRepository, MessageNotifier notifier) {
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
        this.notifier = notifier;
    }

    public OrderResult execute(String eventId, int quantity) {
        return execute(eventId, quantity, null, null);
    }

    public OrderResult execute(String eventId, int quantity, String customerName, String customerEmail) {
        EventId id = new EventId(eventId);
        TicketQuantity tickets = new TicketQuantity(quantity);

        Event event = eventRepository.findById(id)
            .orElseThrow(() -> new EventNotFoundException("Event not found: " + id.value()));

        event.reserveTickets(tickets);
        eventRepository.save(event);

        for (int i = 0; i < quantity; i++) {
            Ticket ticket = new Ticket(
                new TicketId(UUID.randomUUID().toString()),
                id,
                customerName != null ? customerName : "anonymous",
                customerEmail != null ? customerEmail : "");
            ticketRepository.save(ticket);
        }

        notifier.send(ADMIN_EMAIL,
            "Order processed for: " + event.getName()
                + " (" + tickets.value() + " tickets), with ID: " + id.value());

        return new OrderResult(id.value(), event.getName(),
            tickets.value(), event.getAvailableTickets());
    }
}
