package com.ticketera.domain.entity;

import com.ticketera.domain.valueobject.CityId;

public class City {

    private CityId id;
    private final String code;
    private String name;

    public City(Long id, String code, String name) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("City code is required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("City name is required");
        }
        this.id = id != null ? new CityId(id) : null;
        this.code = code.trim();
        this.name = name.trim();
    }

    public CityId getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = new CityId(id);
    }

    public String getCode() {
        return code;
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
