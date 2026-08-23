package com.ticketera.application.usecase;

import com.ticketera.application.port.MessageNotifier;
import com.ticketera.domain.entity.Event;
import com.ticketera.domain.exception.EventNotFoundException;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.valueobject.EventId;
import com.ticketera.domain.valueobject.TicketQuantity;

public class ProcessOrderUseCase {

    private static final String ADMIN_EMAIL = "admin@ticketera.com";

    private final EventRepository repository;
    private final MessageNotifier notifier;

    public ProcessOrderUseCase(EventRepository repository, MessageNotifier notifier) {
        this.repository = repository;
        this.notifier = notifier;
    }

    public OrderResult execute(String eventId, int quantity) {
        EventId id = new EventId(eventId);
        TicketQuantity tickets = new TicketQuantity(quantity);

        Event event = repository.findById(id)
            .orElseThrow(() -> new EventNotFoundException("Event not found: " + id.value()));

        event.reserveTickets(tickets);
        repository.save(event);

        notifier.send(ADMIN_EMAIL,
            "Order processed for: " + event.getName()
                + " (" + tickets.value() + " tickets), with ID: " + id.value());

        return new OrderResult(id.value(), event.getName(),
            tickets.value(), event.getAvailableTickets());
    }
}