package com.ticketera.application.usecase;

import com.ticketera.domain.entity.City;
import com.ticketera.domain.repository.CityRepository;

public class UpdateCityUseCase {

    private final CityRepository repository;

    public UpdateCityUseCase(CityRepository repository) {
        this.repository = repository;
    }

    public City execute(Long id, String newName) {
        City city = repository.findById(id)
            .orElseThrow(() -> new com.ticketera.domain.exception.CityNotFoundException(
                "City with id '" + id + "' not found"));
        city.rename(newName);
        repository.save(city);
        return city;
    }
}
