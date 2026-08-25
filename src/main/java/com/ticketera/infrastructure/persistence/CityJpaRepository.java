package com.ticketera.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CityJpaRepository extends JpaRepository<CityEntity, Long> {

    boolean existsByCode(String code);
}
