package com.ticketera.application.usecase;

import com.ticketera.domain.entity.City;
import com.ticketera.domain.repository.CityRepository;

public class CreateCityUseCase {

    private final CityRepository repository;

    public CreateCityUseCase(CityRepository repository) {
        this.repository = repository;
    }

    public City execute(String code, String name) {
        City city = new City(null, code, name);
        Long id = repository.save(city);
        city.setId(id);
        return city;
    }
}
