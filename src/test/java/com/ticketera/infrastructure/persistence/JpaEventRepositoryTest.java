package com.ticketera.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.valueobject.EventId;
import com.ticketera.domain.valueobject.TicketQuantity;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("dev")
class JpaEventRepositoryTest {

    @Autowired
    private EventJpaRepository jpaRepository;

    private JpaEventRepository repository;

    @BeforeEach
    void setUp() {
        repository = new JpaEventRepository(jpaRepository);
        jpaRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        jpaRepository.deleteAll();
    }

    @Test
    void persistsAndRecoversAggregateWithInventory() {
        Event event = Event.reconstitute(new EventId("evt-test-1"), "Jazz Night", "Teatro", 100, 90);

        repository.save(event);
        Event recovered = repository.findById(new EventId("evt-test-1")).orElseThrow();

        assertEquals("Jazz Night", recovered.getName());
        assertEquals(100, recovered.getCapacity());
        assertEquals(90, recovered.getAvailableTickets());
        assertEquals(10, recovered.getTicketSold());
    }

    @Test
    void persistsReservationsMadeOnAggregate() {
        Event event = Event.reconstitute(new EventId("evt-test-2"), "Rock Fest", "Estadio", 1000, 500);

        event.reserveTickets(new TicketQuantity(200));
        repository.save(event);
        Event recovered = repository.findById(new EventId("evt-test-2")).orElseThrow();

        assertEquals(300, recovered.getAvailableTickets());
        assertEquals(700, recovered.getTicketSold());
    }

    @Test
    void listsAllPersistedEvents() {
        repository.save(Event.reconstitute(new EventId("evt-a"), "A", "V1", 10, 10));
        repository.save(Event.reconstitute(new EventId("evt-b"), "B", "V2", 20, 15));

        List<Event> all = repository.findAll();

        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(e -> e.getId().value().equals("evt-b")));
    }
}
