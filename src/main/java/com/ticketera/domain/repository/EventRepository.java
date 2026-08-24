package com.ticketera.domain.repository;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.valueobject.EventId;

import java.util.List;
import java.util.Optional;

public interface EventRepository {

    Optional<Event> findById(EventId id);

    List<Event> findAll();

    void save(Event event);

    void deleteById(EventId id);
}