package com.ticketera.application.usecase;

import com.ticketera.domain.repository.CityRepository;

public class DeleteCityUseCase {

    private final CityRepository repository;

    public DeleteCityUseCase(CityRepository repository) {
        this.repository = repository;
    }

    public void execute(Long id) {
        repository.deleteById(id);
    }
}
