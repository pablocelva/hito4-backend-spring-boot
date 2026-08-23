package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.exception.EventNotFoundException;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.valueobject.EventId;

public class GetEventDetailsUseCase {

    private final EventRepository repository;

    public GetEventDetailsUseCase(EventRepository repository) {
        this.repository = repository;
    }

    public Event execute(String eventId) {
        EventId id = new EventId(eventId);
        return repository.findById(id)
            .orElseThrow(() -> new EventNotFoundException("Event not found: " + id.value()));
    }
}