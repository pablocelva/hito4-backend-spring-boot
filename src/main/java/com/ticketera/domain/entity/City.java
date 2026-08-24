package com.ticketera.domain.entity;

import com.ticketera.domain.valueobject.CityId;

public class City {

    private final CityId id;
    private String name;

    public City(CityId id, String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("City name is required");
        }
        this.id = id;
        this.name = name.trim();
    }

    public CityId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void rename(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("City name is required");
        }
        this.name = newName.trim();
    }
}