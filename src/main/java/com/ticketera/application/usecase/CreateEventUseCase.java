package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.valueobject.EventId;

import java.util.UUID;

public class CreateEventUseCase {

    private final EventRepository repository;

    public CreateEventUseCase(EventRepository repository) {
        this.repository = repository;
    }

    public Event execute(String name, String venue, int capacity) {
        Event event = new Event(new EventId(UUID.randomUUID().toString()), name, venue, capacity);
        repository.save(event);
        return event;
    }
}