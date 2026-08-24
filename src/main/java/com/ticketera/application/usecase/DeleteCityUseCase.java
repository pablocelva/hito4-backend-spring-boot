package com.ticketera.application.usecase;

import com.ticketera.domain.repository.CityRepository;
import com.ticketera.domain.valueobject.CityId;

public class DeleteCityUseCase {

    private final CityRepository repository;

    public DeleteCityUseCase(CityRepository repository) {
        this.repository = repository;
    }

    public void execute(String id) {
        repository.deleteById(new CityId(id));
    }
}