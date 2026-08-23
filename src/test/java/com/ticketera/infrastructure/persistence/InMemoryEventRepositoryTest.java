package com.ticketera.infrastructure.persistence;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.valueobject.EventId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("In Memory Event Repository")
public class InMemoryEventRepositoryTest {
    @Test
    @DisplayName("Should return empty when event does not exist")
    public void shouldReturnEmptyWhenEventDoesNotExist() {
        InMemoryEventRepository repository = new InMemoryEventRepository();
        assertTrue(repository.findById(new EventId("EVT-999")).isEmpty());
    }

    @Test
    @DisplayName("Should save and find an event")
    public void shouldSaveAndFindEvent() {
        InMemoryEventRepository repository = new InMemoryEventRepository();
        Event event = new Event(new EventId("EVT-001"), "Jazz Night", "Jazz Club", 500);
        repository.save(event);

        Optional<Event> found = repository.findById(new EventId("EVT-001"));
        assertTrue(found.isPresent());
        assertEquals("Jazz Night", found.get().getName());
    }
}