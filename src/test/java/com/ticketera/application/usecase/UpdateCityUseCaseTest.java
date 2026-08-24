package com.ticketera.application.usecase;

import com.ticketera.domain.entity.City;
import com.ticketera.domain.exception.CityNotFoundException;
import com.ticketera.domain.repository.CityRepository;
import com.ticketera.domain.valueobject.CityId;
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
        City city = new City(new CityId("LIM"), "Lima");
        when(repository.findById(any())).thenReturn(Optional.of(city));

        City result = useCase.execute("LIM", "Lima Metropolitana");

        assertEquals("Lima Metropolitana", result.getName());
        verify(repository).save(city);
    }

    @Test
    @DisplayName("Throws CityNotFoundException when city does not exist")
    void throwsCityNotFoundWhenCityDoesNotExist() {
        when(repository.findById(any())).thenReturn(Optional.empty());

        CityNotFoundException ex = assertThrows(CityNotFoundException.class,
            () -> useCase.execute("MISS", "New Name"));
        assertEquals("City with id 'MISS' not found", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Throws when name is blank")
    void throwsWhenNameIsBlank() {
        City city = new City(new CityId("LIM"), "Lima");
        when(repository.findById(any())).thenReturn(Optional.of(city));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> useCase.execute("LIM", "  "));
        assertEquals("City name is required", ex.getMessage());
        verify(repository, never()).save(any());
    }
}
