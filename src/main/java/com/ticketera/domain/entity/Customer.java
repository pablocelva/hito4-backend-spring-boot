package com.ticketera.domain.entity;

import com.ticketera.domain.valueobject.Email;

public class Customer {
    private final String id;
    private final String name;
    private final Email email;

    public Customer(String id, String name, Email email) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Customer ID cannot be blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Customer name cannot be blank");
        }
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }
}