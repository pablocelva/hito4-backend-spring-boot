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

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.valueobject.EventId;
import com.ticketera.domain.valueobject.TicketQuantity;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
        // Arrange
        Event event = Event.reconstitute(new EventId("evt-test-1"), "Jazz Night", "Teatro", 100, 90);

        // Act
        repository.save(event);
        Event recovered = repository.findById(new EventId("evt-test-1")).orElseThrow();

        // Assert
        assertEquals("Jazz Night", recovered.getName());
        assertEquals(100, recovered.getCapacity());
        assertEquals(90, recovered.getAvailableTickets());
        assertEquals(10, recovered.getTicketSold());
    }

    @Test
    void persistsReservationsMadeOnAggregate() {
        // Arrange
        Event event = Event.reconstitute(new EventId("evt-test-2"), "Rock Fest", "Estadio", 1000, 500);

        // Act
        event.reserveTickets(new TicketQuantity(200));
        repository.save(event);
        Event recovered = repository.findById(new EventId("evt-test-2")).orElseThrow();

        // Assert
        assertEquals(300, recovered.getAvailableTickets());
        assertEquals(700, recovered.getTicketSold());
    }

    @Test
    void listsAllPersistedEvents() {
        // Arrange
        repository.save(Event.reconstitute(new EventId("evt-a"), "A", "V1", 10, 10));
        repository.save(Event.reconstitute(new EventId("evt-b"), "B", "V2", 20, 15));

        // Act
        List<Event> all = repository.findAll();

        // Assert
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(e -> e.getId().value().equals("evt-b")));
    }
}