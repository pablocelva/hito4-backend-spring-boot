package com.ticketera.domain.entity;

import com.ticketera.domain.valueobject.CityId;
import com.ticketera.domain.valueobject.EventId;
import com.ticketera.domain.valueobject.TicketQuantity;

public class Event {
    private Long id;
    private final EventId code;
    private CityId cityId;
    private String name;
    private String venue;
    private int capacity;
    private final TicketPool ticketPool;

    public Event(String code, String name, String venue, int capacity) {
        this.code = new EventId(code);
        this.cityId = new CityId(1L);
        this.name = name;
        this.venue = venue;
        this.capacity = capacity;
        this.ticketPool = new TicketPool(capacity);
    }

    public static Event reconstitute(Long id, EventId code, String name, String venue, int capacity, int availableTickets) {
        return new Event(id, code, name, venue, capacity, availableTickets, 1L);
    }

    public static Event reconstitute(Long id, EventId code, CityId cityId, String name, String venue, int capacity, int availableTickets) {
        return new Event(id, code, name, venue, capacity, availableTickets, cityId.value());
    }

    private Event(Long id, EventId code, String name, String venue, int capacity, int availableTickets, Long cityId) {
        this.id = id;
        this.code = code;
        this.cityId = new CityId(cityId);
        this.name = name;
        this.venue = venue;
        this.capacity = capacity;
        this.ticketPool = new TicketPool(capacity, availableTickets);
    }

    public Long getDbId() {
        return id;
    }

    public void setDbId(Long id) {
        this.id = id;
    }

    public EventId getCode() {
        return code;
    }

    public CityId getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = new CityId(cityId);
    }

    public String getName() {
        return name;
    }

    public String getVenue() {
        return venue;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean hasAvailability() {
        return ticketPool.hasAvailability();
    }

    public int getAvailableTickets() {
        return ticketPool.getAvailable();
    }

    public int getTicketSold() {
        return capacity - ticketPool.getAvailable();
    }

    public boolean hasSoldTickets() {
        return ticketPool.getAvailable() < capacity;
    }

    public void reserveTickets(TicketQuantity quantity) {
        ticketPool.reserve(quantity);
    }

    public void updateDetails(String name, String venue, int capacity) {
        if (capacity < getTicketSold()) {
            throw new com.ticketera.domain.exception.InvalidOrderException(
                "New capacity (" + capacity + ") cannot be less than sold tickets (" + getTicketSold() + ")");
        }
        this.name = name;
        this.venue = venue;
        this.capacity = capacity;
    }
}
