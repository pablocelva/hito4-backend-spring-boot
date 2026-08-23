package com.ticketera.infrastructure.web;

import com.ticketera.application.usecase.CreateEventUseCase;
import com.ticketera.application.usecase.GetEventDetailsUseCase;
import com.ticketera.application.usecase.GetEventsUseCase;
import com.ticketera.infrastructure.web.dto.CreateEventRequest;
import com.ticketera.infrastructure.web.dto.EventResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final GetEventsUseCase getEventsUseCase;
    private final GetEventDetailsUseCase getEventDetailsUseCase;
    private final CreateEventUseCase createEventUseCase;

    public EventController(GetEventsUseCase getEventsUseCase,
                           GetEventDetailsUseCase getEventDetailsUseCase,
                           CreateEventUseCase createEventUseCase) {
        this.getEventsUseCase = getEventsUseCase;
        this.getEventDetailsUseCase = getEventDetailsUseCase;
        this.createEventUseCase = createEventUseCase;
    }

    @GetMapping
    public List<EventResponse> listEvents() {
        return getEventsUseCase.execute().stream()
            .map(EventResponse::fromDomain)
            .toList();
    }

    @GetMapping("/{id}")
    public EventResponse getEvent(@PathVariable String id) {
        return EventResponse.fromDomain(getEventDetailsUseCase.execute(id));
    }

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody CreateEventRequest request) {
        var event = createEventUseCase.execute(request.name(), request.venue(), request.capacity());
        return ResponseEntity.status(HttpStatus.CREATED).body(EventResponse.fromDomain(event));
    }
}