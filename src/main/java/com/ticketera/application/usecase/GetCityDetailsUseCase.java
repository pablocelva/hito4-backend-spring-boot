package com.ticketera.application.usecase;

import com.ticketera.domain.entity.City;
import com.ticketera.domain.repository.CityRepository;
import com.ticketera.domain.valueobject.CityId;

public class GetCityDetailsUseCase {

    private final CityRepository repository;

    public GetCityDetailsUseCase(CityRepository repository) {
        this.repository = repository;
    }

    public City execute(String id) {
        return repository.findById(new CityId(id))
            .orElseThrow(() -> new com.ticketera.domain.exception.CityNotFoundException(
                "City with id '" + id + "' not found"));
    }
}