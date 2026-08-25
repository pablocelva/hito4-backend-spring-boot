package com.ticketera.infrastructure.persistence;

import com.ticketera.domain.entity.City;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cities")
public class CityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    protected CityEntity() {
    }

    private CityEntity(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public static CityEntity fromDomain(City city) {
        Long dbId = city.getId() != null ? city.getId().value() : null;
        return new CityEntity(dbId, city.getCode(), city.getName());
    }

    public City toDomain() {
        return new City(id, code, name);
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
