package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.exception.EventNotFoundException;
import com.ticketera.domain.exception.InvalidOrderException;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.valueobject.EventId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateEventUseCaseTest {

    private EventRepository repository;
    private UpdateEventUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(EventRepository.class);
        useCase = new UpdateEventUseCase(repository);
    }

    @Test
    @DisplayName("Updates event details successfully")
    void updatesEventDetails() {
        Event event = Event.reconstitute(new EventId("evt-1"), "Jazz Night", "Teatro", 100, 100);
        when(repository.findById(new EventId("evt-1"))).thenReturn(Optional.of(event));

        Event result = useCase.execute("evt-1", "Rock Night", "Estadio", 500);

        assertEquals("Rock Night", result.getName());
        assertEquals("Estadio", result.getVenue());
        assertEquals(500, result.getCapacity());
        verify(repository).save(event);
    }

    @Test
    @DisplayName("Throws EventNotFoundException when event does not exist")
    void throwsEventNotFound() {
        when(repository.findById(any())).thenReturn(Optional.empty());

        EventNotFoundException ex = assertThrows(EventNotFoundException.class,
            () -> useCase.execute("missing", "New", "Venue", 100));
        assertEquals("Event not found: missing", ex.getMessage());
    }

    @Test
    @DisplayName("Throws when capacity is less than sold tickets")
    void throwsWhenCapacityLessThanSold() {
        Event event = Event.reconstitute(new EventId("evt-1"), "Jazz", "Teatro", 100, 80);
        when(repository.findById(new EventId("evt-1"))).thenReturn(Optional.of(event));

        InvalidOrderException ex = assertThrows(InvalidOrderException.class,
            () -> useCase.execute("evt-1", "Small", "Venue", 10));
        assertTrue(ex.getMessage().contains("cannot be less than sold tickets"));
        verify(repository, never()).save(any());
    }
}
