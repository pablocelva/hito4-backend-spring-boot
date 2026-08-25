package com.ticketera.infrastructure.persistence;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.repository.EventRepository;
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
    public Optional<Event> findById(Long id) {
        return jpaRepository.findById(id).map(EventEntity::toDomain);
    }

    @Override
    public Optional<Event> findByCode(String code) {
        return jpaRepository.findAll().stream()
            .filter(e -> e.getCode().equals(code))
            .findFirst()
            .map(EventEntity::toDomain);
    }

    @Override
    public List<Event> findAll() {
        return jpaRepository.findAll().stream()
            .map(EventEntity::toDomain)
            .toList();
    }

    @Override
    public Long save(Event event) {
        EventEntity saved = jpaRepository.save(EventEntity.fromDomain(event));
        return saved.getId();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
