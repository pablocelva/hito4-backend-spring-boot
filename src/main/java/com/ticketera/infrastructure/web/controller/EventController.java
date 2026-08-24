package com.ticketera.infrastructure.web.controller;

import com.ticketera.application.usecase.CreateEventUseCase;
import com.ticketera.application.usecase.GetEventDetailsUseCase;
import com.ticketera.application.usecase.GetEventsUseCase;
import com.ticketera.infrastructure.web.dto.CreateEventRequest;
import com.ticketera.infrastructure.web.dto.EventResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Events", description = "Cartelera de eventos: consulta, detalle y creacion")
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

    @Operation(summary = "Listar eventos", description = "Retorna la cartelera completa de eventos")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Cartelera obtenida"))
    @GetMapping
    public List<EventResponse> listEvents() {
        return getEventsUseCase.execute().stream()
            .map(EventResponse::fromDomain)
            .toList();
    }

    @Operation(summary = "Detalle de evento", description = "Retorna un evento por su identificador")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evento encontrado"),
        @ApiResponse(responseCode = "404", description = "Evento no existe")
    })
    @GetMapping("/{id}")
    public EventResponse getEvent(@PathVariable String id) {
        return EventResponse.fromDomain(getEventDetailsUseCase.execute(id));
    }

    @Operation(summary = "Crear evento", description = "Registra un nuevo evento con su capacidad inicial")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Evento creado"),
        @ApiResponse(responseCode = "400", description = "Payload invalido")
    })
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody CreateEventRequest request) {
        var event = createEventUseCase.execute(request.name(), request.venue(), request.capacity());
        return ResponseEntity.status(HttpStatus.CREATED).body(EventResponse.fromDomain(event));
    }
}