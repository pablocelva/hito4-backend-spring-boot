package com.ticketera.domain.entity;

import com.ticketera.domain.valueobject.EventId;
import com.ticketera.domain.valueobject.TicketId;

public class Ticket {

    private final TicketId id;
    private final EventId eventId;
    private final String customerName;
    private final String customerEmail;

    public Ticket(TicketId id, EventId eventId, String customerName, String customerEmail) {
        this.id = id;
        this.eventId = eventId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
    }

    public TicketId getId() {
        return id;
    }

    public EventId getEventId() {
        return eventId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }
}
