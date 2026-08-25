package com.ticketera.application.usecase;

import com.ticketera.domain.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        useCase.execute(1L);

        verify(repository).deleteById(1L);
    }
}
