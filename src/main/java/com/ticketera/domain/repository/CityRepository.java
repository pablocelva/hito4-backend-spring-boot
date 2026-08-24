package com.ticketera.domain.repository;

import com.ticketera.domain.entity.City;
import com.ticketera.domain.valueobject.CityId;

import java.util.List;
import java.util.Optional;

public interface CityRepository {

    Optional<City> findById(CityId id);

    List<City> findAll();

    void save(City city);

    void deleteById(CityId id);
}