package com.ticketera.application.usecase;

import com.ticketera.domain.entity.City;
import com.ticketera.domain.repository.CityRepository;

public class GetCityDetailsUseCase {

    private final CityRepository repository;

    public GetCityDetailsUseCase(CityRepository repository) {
        this.repository = repository;
    }

    public City execute(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new com.ticketera.domain.exception.CityNotFoundException(
                "City with id '" + id + "' not found"));
    }
}
