package com.ticketera.domain.repository;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.valueobject.EventId;
import java.util.Optional;

public interface EventRepository {
    Optional<Event> findById(EventId id);
    void save(Event event);
}