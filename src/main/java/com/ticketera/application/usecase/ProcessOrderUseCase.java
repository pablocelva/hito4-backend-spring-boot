package com.ticketera.application.usecase;

import com.ticketera.application.port.MessageNotifier ;
import com.ticketera.domain.entity.Event;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.valueobject.EventId;
import com.ticketera.domain.valueobject.TicketQuantity;

public class ProcessOrderUseCase {
    private final EventRepository repository;
    private final MessageNotifier  notifier;

    public ProcessOrderUseCase(EventRepository repository, MessageNotifier  notifier) {
        this.repository = repository;
        this.notifier = notifier;
    }

    public void execute(String eventId, int quantity) {
        EventId id = new EventId(eventId);
        TicketQuantity tickets = new TicketQuantity(quantity);
        Event event = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        event.reserveTickets(tickets);
        notifier.send("admin@ticketera.com",
            "Order processed for: " + event.getName() + " (" + tickets.value() + " tickets), with ID: " + id.value());
    }
}
