package com.ticketera.domain.repository;

import com.ticketera.domain.entity.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository {

    Optional<Event> findById(Long id);

    Optional<Event> findByCode(String code);

    List<Event> findAll();

    Long save(Event event);

    void deleteById(Long id);
}
