package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.exception.EventNotFoundException;
import com.ticketera.domain.repository.EventRepository;

public class GetEventDetailsUseCase {

    private final EventRepository repository;

    public GetEventDetailsUseCase(EventRepository repository) {
        this.repository = repository;
    }

    public Event execute(Long eventId) {
        return repository.findById(eventId)
            .orElseThrow(() -> new EventNotFoundException("Event not found: " + eventId));
    }
}
