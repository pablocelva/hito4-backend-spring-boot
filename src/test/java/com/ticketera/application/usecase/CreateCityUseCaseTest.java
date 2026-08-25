package com.ticketera.application.usecase;

import com.ticketera.domain.entity.City;
import com.ticketera.domain.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateCityUseCaseTest {

    private CityRepository repository;
    private CreateCityUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(CityRepository.class);
        useCase = new CreateCityUseCase(repository);
    }

    @Test
    @DisplayName("Creates city successfully")
    void createsCitySuccessfully() {
        City result = useCase.execute("LIM", "Lima");

        assertEquals("LIM", result.getCode());
        assertEquals("Lima", result.getName());
        verify(repository).save(any(City.class));
    }

    @Test
    @DisplayName("Throws when code is null")
    void throwsWhenCodeIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> useCase.execute(null, "Lima"));
        assertEquals("City code is required", ex.getMessage());
    }

    @Test
    @DisplayName("Throws when name is blank")
    void throwsWhenNameIsBlank() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
            () -> useCase.execute("LIM", "  "));
        assertEquals("City name is required", ex.getMessage());
    }
}
