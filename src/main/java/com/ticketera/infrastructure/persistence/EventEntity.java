package com.ticketera.infrastructure.persistence;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.valueobject.CityId;
import com.ticketera.domain.valueobject.EventId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "events")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "city_id", nullable = false)
    private Long cityId;

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

    private EventEntity(Long id, String code, Long cityId, String name, String venue, int capacity, int availableTickets) {
        this.id = id;
        this.code = code;
        this.cityId = cityId;
        this.name = name;
        this.venue = venue;
        this.capacity = capacity;
        this.availableTickets = availableTickets;
    }

    public static EventEntity fromDomain(Event event) {
        Long dbId = event.getDbId();
        return new EventEntity(
            dbId,
            event.getCode().value(),
            event.getCityId().value(),
            event.getName(),
            event.getVenue(),
            event.getCapacity(),
            event.getAvailableTickets());
    }

    public Event toDomain() {
        return Event.reconstitute(
            id,
            new EventId(code),
            new CityId(cityId),
            name, venue, capacity, availableTickets);
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }
}
