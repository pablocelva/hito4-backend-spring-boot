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

class DeleteEventUseCaseTest {

    private EventRepository repository;
    private DeleteEventUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(EventRepository.class);
        useCase = new DeleteEventUseCase(repository);
    }

    @Test
    @DisplayName("Deletes event without sold tickets")
    void deletesEventWithoutSoldTickets() {
        Event event = Event.reconstitute(1L, new EventId("evt-1"), "Jazz", "Teatro", 100, 100);
        when(repository.findById(1L)).thenReturn(Optional.of(event));

        useCase.execute(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("Throws EventNotFoundException when event does not exist")
    void throwsEventNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());
        EventNotFoundException ex = assertThrows(EventNotFoundException.class,
            () -> useCase.execute(999L));
        assertEquals("Event not found: 999", ex.getMessage());
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Throws when event has sold tickets")
    void throwsWhenEventHasSoldTickets() {
        Event event = Event.reconstitute(1L, new EventId("evt-1"), "Jazz", "Teatro", 100, 80);
        when(repository.findById(1L)).thenReturn(Optional.of(event));

        InvalidOrderException ex = assertThrows(InvalidOrderException.class,
            () -> useCase.execute(1L));
        assertEquals("Cannot delete event with sold tickets", ex.getMessage());
        verify(repository, never()).deleteById(any());
    }
}
