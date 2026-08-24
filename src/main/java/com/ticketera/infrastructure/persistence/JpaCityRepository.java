package com.ticketera.infrastructure.persistence;

import com.ticketera.domain.entity.City;
import com.ticketera.domain.repository.CityRepository;
import com.ticketera.domain.valueobject.CityId;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaCityRepository implements CityRepository {

    private final CityJpaRepository jpaRepository;

    public JpaCityRepository(CityJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<City> findById(CityId id) {
        return jpaRepository.findById(id.value()).map(CityEntity::toDomain);
    }

    @Override
    public List<City> findAll() {
        return jpaRepository.findAll().stream().map(CityEntity::toDomain).toList();
    }

    @Override
    public void save(City city) {
        jpaRepository.save(CityEntity.fromDomain(city));
    }

    @Override
    public void deleteById(CityId id) {
        jpaRepository.deleteById(id.value());
    }
}