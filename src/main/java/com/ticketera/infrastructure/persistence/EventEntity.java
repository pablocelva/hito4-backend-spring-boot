package com.ticketera.infrastructure.persistence;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.valueobject.EventId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "events")
public class EventEntity {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "city_id", nullable = false, length = 50)
    private String cityId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "venue", nullable = false)
    private String venue;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "available_tickets", nullable = false)
    private int availableTickets;

    protected EventEntity() {
    }

    private EventEntity(String id, String cityId, String name, String venue, int capacity, int availableTickets) {
        this.id = id;
        this.cityId = cityId;
        this.name = name;
        this.venue = venue;
        this.capacity = capacity;
        this.availableTickets = availableTickets;
    }

    public static EventEntity fromDomain(Event event) {
        return new EventEntity(
            event.getId().value(),
            event.getCityId().value(),
            event.getName(),
            event.getVenue(),
            event.getCapacity(),
            event.getAvailableTickets());
    }

    public Event toDomain() {
        return Event.reconstitute(
            new EventId(id),
            new com.ticketera.domain.valueobject.CityId(cityId),
            name, venue, capacity, availableTickets);
    }

    public String getId() {
        return id;
    }
}
