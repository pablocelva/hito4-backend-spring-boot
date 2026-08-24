package com.ticketera.application.usecase;

import com.ticketera.domain.entity.City;
import com.ticketera.domain.repository.CityRepository;
import com.ticketera.domain.valueobject.CityId;

public class CreateCityUseCase {

    private final CityRepository repository;

    public CreateCityUseCase(CityRepository repository) {
        this.repository = repository;
    }

    public City execute(String id, String name) {
        City city = new City(new CityId(id), name);
        repository.save(city);
        return city;
    }
}