package com.ticketera.application.usecase;

import com.ticketera.domain.entity.City;
import com.ticketera.domain.repository.CityRepository;

import java.util.List;

public class GetCitiesUseCase {

    private final CityRepository repository;

    public GetCitiesUseCase(CityRepository repository) {
        this.repository = repository;
    }

    public List<City> execute() {
        return repository.findAll();
    }
}