package com.ticketera.domain.entity;

import com.ticketera.domain.valueobject.TicketId;

public class Ticket {

    private final TicketId id;
    private final Long eventId;
    private final String customerName;
    private final String customerEmail;

    public Ticket(TicketId id, Long eventId, String customerName, String customerEmail) {
        this.id = id;
        this.eventId = eventId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
    }

    public TicketId getId() {
        return id;
    }

    public Long getEventId() {
        return eventId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }
}
