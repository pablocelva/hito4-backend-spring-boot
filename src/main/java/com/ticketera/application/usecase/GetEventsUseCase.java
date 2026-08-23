package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.repository.EventRepository;

import java.util.List;

public class GetEventsUseCase {

    private final EventRepository repository;

    public GetEventsUseCase(EventRepository repository) {
        this.repository = repository;
    }

    public List<Event> execute() {
        return repository.findAll();
    }
}