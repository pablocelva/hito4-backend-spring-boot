package com.ticketera.infrastructure.persistence;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.valueobject.EventId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryEventRepository implements EventRepository {
    private final Map<EventId, Event> events = new HashMap<>();

    @Override
    public Optional<Event> findById(EventId id) {
        return Optional.ofNullable(events.get(id));
    }

    @Override
    public void save(Event event) {
        events.put(event.getId(), event);
    }
}
