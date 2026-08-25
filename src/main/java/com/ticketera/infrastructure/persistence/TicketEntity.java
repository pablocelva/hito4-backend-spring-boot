package com.ticketera.infrastructure.persistence;

import com.ticketera.domain.entity.Ticket;
import com.ticketera.domain.valueobject.TicketId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tickets")
public class TicketEntity {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_email")
    private String customerEmail;

    protected TicketEntity() {
    }

    private TicketEntity(String id, Long eventId, String customerName, String customerEmail) {
        this.id = id;
        this.eventId = eventId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
    }

    public static TicketEntity fromDomain(Ticket ticket) {
        return new TicketEntity(
            ticket.getId().value(),
            ticket.getEventId(),
            ticket.getCustomerName(),
            ticket.getCustomerEmail());
    }

    public Ticket toDomain() {
        return new Ticket(
            new TicketId(id),
            eventId,
            customerName,
            customerEmail);
    }

    public String getId() {
        return id;
    }
}
