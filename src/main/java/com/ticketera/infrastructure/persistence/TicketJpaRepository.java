package com.ticketera.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketJpaRepository extends JpaRepository<TicketEntity, String> {

    List<TicketEntity> findByEventId(String eventId);
}
