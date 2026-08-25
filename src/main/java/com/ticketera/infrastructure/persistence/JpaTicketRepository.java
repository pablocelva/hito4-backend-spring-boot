package com.ticketera.infrastructure.persistence;

import com.ticketera.domain.entity.Ticket;
import com.ticketera.domain.repository.TicketRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaTicketRepository implements TicketRepository {

    private final TicketJpaRepository jpaRepository;

    public JpaTicketRepository(TicketJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Ticket> findByEventId(Long eventId) {
        return jpaRepository.findByEventId(eventId).stream()
            .map(TicketEntity::toDomain)
            .toList();
    }

    @Override
    public void save(Ticket ticket) {
        jpaRepository.save(TicketEntity.fromDomain(ticket));
    }
}
