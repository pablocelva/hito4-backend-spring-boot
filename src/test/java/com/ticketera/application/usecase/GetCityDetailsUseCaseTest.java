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

class GetCityDetailsUseCaseTest {

    private CityRepository repository;
    private GetCityDetailsUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(CityRepository.class);
        useCase = new GetCityDetailsUseCase(repository);
    }

    @Test
    @DisplayName("Returns city when found")
    void returnsCityWhenFound() {
        City city = new City(new CityId("LIM"), "Lima");
        when(repository.findById(any())).thenReturn(Optional.of(city));

        City result = useCase.execute("LIM");

        assertEquals("LIM", result.getId().value());
        assertEquals("Lima", result.getName());
    }

    @Test
    @DisplayName("Throws CityNotFoundException when missing")
    void throwsCityNotFoundWhenMissing() {
        when(repository.findById(any())).thenReturn(Optional.empty());

        CityNotFoundException ex = assertThrows(CityNotFoundException.class,
            () -> useCase.execute("MISS"));
        assertEquals("City with id 'MISS' not found", ex.getMessage());
    }
}
