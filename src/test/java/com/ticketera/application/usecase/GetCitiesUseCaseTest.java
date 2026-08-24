package com.ticketera.application.usecase;

import com.ticketera.domain.entity.City;
import com.ticketera.domain.repository.CityRepository;
import com.ticketera.domain.valueobject.CityId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GetCitiesUseCaseTest {

    private CityRepository repository;
    private GetCitiesUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(CityRepository.class);
        useCase = new GetCitiesUseCase(repository);
    }

    @Test
    @DisplayName("Returns all cities")
    void returnsAllCities() {
        List<City> expected = List.of(new City(new CityId("LIM"), "Lima"));
        when(repository.findAll()).thenReturn(expected);

        List<City> result = useCase.execute();

        assertEquals(expected, result);
        verify(repository).findAll();
    }
}
