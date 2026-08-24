package com.ticketera.domain.entity;

import com.ticketera.domain.valueobject.CityId;
import com.ticketera.domain.valueobject.EventId;
import com.ticketera.domain.valueobject.TicketQuantity;

public class Event {
    private final EventId id;
    private final CityId cityId;
    private String name;
    private String venue;
    private int capacity;
    private final TicketPool ticketPool;

    public Event(EventId id, String name, String venue, int capacity) {
        this.id = id;
        this.cityId = new CityId("LIM");
        this.name = name;
        this.venue = venue;
        this.capacity = capacity;
        this.ticketPool = new TicketPool(capacity);
    }

    public static Event reconstitute(EventId id, String name, String venue, int capacity, int availableTickets) {
        return new Event(id, name, venue, capacity, availableTickets, "LIM");
    }

    public static Event reconstitute(EventId id, CityId cityId, String name, String venue, int capacity, int availableTickets) {
        return new Event(id, name, venue, capacity, availableTickets, cityId.value());
    }

    private Event(EventId id, String name, String venue, int capacity, int availableTickets, String cityId) {
        this.id = id;
        this.cityId = new CityId(cityId);
        this.name = name;
        this.venue = venue;
        this.capacity = capacity;
        this.ticketPool = new TicketPool(capacity, availableTickets);
    }

    public EventId getId() {
        return id;
    }

    public CityId getCityId() {
        return cityId;
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
