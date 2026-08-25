package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.exception.InvalidOrderException;
import com.ticketera.domain.repository.EventRepository;

public class DeleteEventUseCase {

    private final EventRepository repository;

    public DeleteEventUseCase(EventRepository repository) {
        this.repository = repository;
    }

    public void execute(Long eventId) {
        Event event = repository.findById(eventId)
            .orElseThrow(() -> new com.ticketera.domain.exception.EventNotFoundException(
                "Event not found: " + eventId));

        if (event.hasSoldTickets()) {
            throw new InvalidOrderException("Cannot delete event with sold tickets");
        }

        repository.deleteById(eventId);
    }
}
