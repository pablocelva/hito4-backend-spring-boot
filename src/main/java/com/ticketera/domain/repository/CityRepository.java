package com.ticketera.domain.repository;

import com.ticketera.domain.entity.City;

import java.util.List;
import java.util.Optional;

public interface CityRepository {

    Optional<City> findById(Long id);

    Optional<City> findByCode(String code);

    List<City> findAll();

    Long save(City city);

    void deleteById(Long id);
}
