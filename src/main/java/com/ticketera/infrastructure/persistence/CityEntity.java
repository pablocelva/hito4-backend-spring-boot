package com.ticketera.infrastructure.persistence;

import com.ticketera.domain.entity.City;
import com.ticketera.domain.valueobject.CityId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cities")
public class CityEntity {

    @Id
    @Column(name = "id", nullable = false, length = 50)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    protected CityEntity() {
    }

    private CityEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CityEntity fromDomain(City city) {
        return new CityEntity(city.getId().value(), city.getName());
    }

    public City toDomain() {
        return new City(new CityId(id), name);
    }

    public String getId() {
        return id;
    }
}