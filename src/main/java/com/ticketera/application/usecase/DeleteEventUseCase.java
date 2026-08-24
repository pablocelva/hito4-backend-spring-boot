package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.exception.InvalidOrderException;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.valueobject.EventId;

public class DeleteEventUseCase {

    private final EventRepository repository;

    public DeleteEventUseCase(EventRepository repository) {
        this.repository = repository;
    }

    public void execute(String eventId) {
        EventId id = new EventId(eventId);
        Event event = repository.findById(id)
            .orElseThrow(() -> new com.ticketera.domain.exception.EventNotFoundException(
                "Event not found: " + id.value()));

        if (event.hasSoldTickets()) {
            throw new InvalidOrderException("Cannot delete event with sold tickets");
        }

        repository.deleteById(id);
    }
}
