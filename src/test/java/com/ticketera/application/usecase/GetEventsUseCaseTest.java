package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.valueobject.EventId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GetEventsUseCaseTest {

    private EventRepository repository;
    private GetEventsUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(EventRepository.class);
        useCase = new GetEventsUseCase(repository);
    }

    @Test
    @DisplayName("Returns all events from repository")
    void returnsAllEventsFromRepository() {
        List<Event> expected = List.of(
            Event.reconstitute(new EventId("evt-1"), "Jazz Night", "Teatro", 100, 90),
            Event.reconstitute(new EventId("evt-2"), "Rock Fest", "Estadio", 1000, 500));
        when(repository.findAll()).thenReturn(expected);

        List<Event> result = useCase.execute();

        assertEquals(expected, result);
        verify(repository).findAll();
    }
}