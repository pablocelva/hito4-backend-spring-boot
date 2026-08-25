package com.ticketera.infrastructure.persistence;

import com.ticketera.domain.entity.City;
import com.ticketera.domain.repository.CityRepository;
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
    public Optional<City> findById(Long id) {
        return jpaRepository.findById(id).map(CityEntity::toDomain);
    }

    @Override
    public Optional<City> findByCode(String code) {
        return jpaRepository.findAll().stream()
            .filter(e -> e.getCode().equals(code))
            .findFirst()
            .map(CityEntity::toDomain);
    }

    @Override
    public List<City> findAll() {
        return jpaRepository.findAll().stream()
            .map(CityEntity::toDomain)
            .toList();
    }

    @Override
    public Long save(City city) {
        CityEntity saved = jpaRepository.save(CityEntity.fromDomain(city));
        return saved.getId();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
