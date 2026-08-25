package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.exception.EventNotFoundException;
import com.ticketera.domain.repository.EventRepository;

public class UpdateEventUseCase {

    private final EventRepository repository;

    public UpdateEventUseCase(EventRepository repository) {
        this.repository = repository;
    }

    public Event execute(Long eventId, String name, String venue, int capacity) {
        Event event = repository.findById(eventId)
            .orElseThrow(() -> new EventNotFoundException("Event not found: " + eventId));

        event.updateDetails(name, venue, capacity);
        repository.save(event);
        return event;
    }
}
