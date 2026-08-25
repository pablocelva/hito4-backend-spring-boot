package com.ticketera.application.usecase;

import com.ticketera.domain.entity.City;
import com.ticketera.domain.exception.CityNotFoundException;
import com.ticketera.domain.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateCityUseCaseTest {

    private CityRepository repository;
    private UpdateCityUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(CityRepository.class);
        useCase = new UpdateCityUseCase(repository);
    }

    @Test
    @DisplayName("Updates city name successfully")
    void updatesCityNameSuccessfully() {
        City city = new City(1L, "LIM", "Lima");
        when(repository.findById(1L)).thenReturn(Optional.of(city));

        City result = useCase.execute(1L, "Lima Metropolitana");

        assertEquals("Lima Metropolitana", result.getName());
        verify(repository).save(city);
    }

    @Test
    @DisplayName("Throws CityNotFoundException when city does not exist")
    void throwsCityNotFoundWhenCityDoesNotExist() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        CityNotFoundException ex = assertThrows(CityNotFoundException.class,
            () -> useCase.execute(999L, "New Name"));
        assertEquals("City with id '999' not found", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Throws when name is blank")
    void throwsWhenNameIsBlank() {
        City city = new City(1L, "LIM", "Lima");
        when(repository.findById(1L)).thenReturn(Optional.of(city));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> useCase.execute(1L, "  "));
        assertEquals("City name is required", ex.getMessage());
        verify(repository, never()).save(any());
    }
}
