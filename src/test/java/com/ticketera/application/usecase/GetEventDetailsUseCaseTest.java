package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.exception.EventNotFoundException;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.valueobject.EventId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetEventDetailsUseCaseTest {

    private EventRepository repository;
    private GetEventDetailsUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(EventRepository.class);
        useCase = new GetEventDetailsUseCase(repository);
    }

    @Test
    @DisplayName("Returns event when found")
    void returnsEventWhenFound() {
        Event event = Event.reconstitute(1L, new EventId("evt-1"), "Jazz Night", "Teatro", 100, 90);
        when(repository.findById(1L)).thenReturn(Optional.of(event));

        assertEquals(event, useCase.execute(1L));
    }

    @Test
    @DisplayName("Throws EventNotFoundException when missing")
    void throwsEventNotFoundWhenMissing() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        EventNotFoundException ex = assertThrows(EventNotFoundException.class,
            () -> useCase.execute(999L));
        assertEquals("Event not found: 999", ex.getMessage());
    }
}
