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

class DeleteCityUseCaseTest {

    private CityRepository repository;
    private DeleteCityUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(CityRepository.class);
        useCase = new DeleteCityUseCase(repository);
    }

    @Test
    @DisplayName("Deletes city successfully")
    void deletesCitySuccessfully() {
        useCase.execute("LIM");

        verify(repository).deleteById(any());
    }
}
