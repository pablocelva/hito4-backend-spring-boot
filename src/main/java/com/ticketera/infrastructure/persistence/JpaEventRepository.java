package com.ticketera.infrastructure.persistence;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.valueobject.EventId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaEventRepository implements EventRepository {

    private final EventJpaRepository jpaRepository;

    public JpaEventRepository(EventJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Event> findById(EventId id) {
        return jpaRepository.findById(id.value()).map(EventEntity::toDomain);
    }

    @Override
    public List<Event> findAll() {
        return jpaRepository.findAll().stream()
            .map(EventEntity::toDomain)
            .toList();
    }

    @Override
    public void save(Event event) {
        jpaRepository.save(EventEntity.fromDomain(event));
    }
}