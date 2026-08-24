package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.exception.EventNotFoundException;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.valueobject.EventId;

public class UpdateEventUseCase {

    private final EventRepository repository;

    public UpdateEventUseCase(EventRepository repository) {
        this.repository = repository;
    }

    public Event execute(String eventId, String name, String venue, int capacity) {
        EventId id = new EventId(eventId);
        Event event = repository.findById(id)
            .orElseThrow(() -> new EventNotFoundException("Event not found: " + id.value()));

        event.updateDetails(name, venue, capacity);
        repository.save(event);
        return event;
    }
}
