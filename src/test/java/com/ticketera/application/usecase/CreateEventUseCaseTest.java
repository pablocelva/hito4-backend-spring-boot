package com.ticketera.application.usecase;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateEventUseCaseTest {

    private EventRepository repository;
    private CreateEventUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(EventRepository.class);
        useCase = new CreateEventUseCase(repository);
    }

    @Test
    @DisplayName("Creates event with generated code and persists it")
    void createsEventWithGeneratedCodeAndPersistsIt() {
        Event result = useCase.execute(1L, "Jazz Night", "Gran Teatro", 500);

        assertNotNull(result.getCode());
        assertEquals("Jazz Night", result.getName());
        assertEquals("Gran Teatro", result.getVenue());
        assertEquals(500, result.getCapacity());
        assertEquals(500, result.getAvailableTickets());
        assertEquals(1L, result.getCityId().value());
        verify(repository).save(any(Event.class));
    }

    @Test
    @DisplayName("Delegates validation to domain")
    void delegatesValidationToDomain() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> useCase.execute(1L, "Jazz Night", "Gran Teatro", 0));
        assertEquals("Capacity must be positive", ex.getMessage());
        verify(repository, never()).save(any());
    }
}
