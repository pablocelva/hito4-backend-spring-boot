package com.ticketera.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateEventRequest(

    @NotBlank(message = "Name is required")
    String name,

    @NotBlank(message = "Venue is required")
    String venue,

    @Positive(message = "Capacity must be positive")
    int capacity
) {
}