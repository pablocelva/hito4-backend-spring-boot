package com.ticketera.infrastructure.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record TicketOrderRequest(

    @NotBlank(message = "Event id is required")
    String eventId,

    @Positive(message = "Quantity must be positive")
    int quantity,

    @Email(message = "Customer email must be valid")
    String customerEmail
) {
}