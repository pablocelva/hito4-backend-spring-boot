package com.ticketera.domain.entity;

import com.ticketera.domain.valueobject.EventId;
import com.ticketera.domain.valueobject.TicketQuantity;

public class Event {
    private final EventId id;
    private final String name;
    private final String venue;
    private final int capacity;
    private final TicketPool ticketPool;

    public Event(EventId id, String name, String venue, int capacity) {
        this.id = id;
        this.name = name;
        this.venue = venue;
        this.capacity = capacity;
        this.ticketPool = new TicketPool(capacity);
    }

    public static Event reconstitute(EventId id, String name, String venue, int capacity, int availableTickets) {
        return new Event(id, name, venue, capacity, availableTickets);
    }
    
    private Event(EventId id, String name, String venue, int capacity, int availableTickets) {
        this.id = id;
        this.name = name;
        this.venue = venue;
        this.capacity = capacity;
        this.ticketPool = new TicketPool(capacity, availableTickets);
    }

    public EventId getId() {
        return id;
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

    public void reserveTickets(TicketQuantity quantity) {
        ticketPool.reserve(quantity);
    }
}
