package com.ticketera.infrastructure.web.controller;

import com.ticketera.application.usecase.CreateEventUseCase;
import com.ticketera.application.usecase.DeleteEventUseCase;
import com.ticketera.application.usecase.GetEventDetailsUseCase;
import com.ticketera.application.usecase.GetEventTicketsUseCase;
import com.ticketera.application.usecase.GetEventsUseCase;
import com.ticketera.application.usecase.UpdateEventUseCase;
import com.ticketera.infrastructure.web.dto.CreateEventRequest;
import com.ticketera.infrastructure.web.dto.EventResponse;
import com.ticketera.infrastructure.web.dto.TicketResponseDto;
import com.ticketera.infrastructure.web.dto.UpdateEventRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Events", description = "Cartelera de eventos: consulta, detalle, creacion, actualizacion y eliminacion")
@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final GetEventsUseCase getEventsUseCase;
    private final GetEventDetailsUseCase getEventDetailsUseCase;
    private final CreateEventUseCase createEventUseCase;
    private final UpdateEventUseCase updateEventUseCase;
    private final DeleteEventUseCase deleteEventUseCase;
    private final GetEventTicketsUseCase getEventTicketsUseCase;

    public EventController(GetEventsUseCase getEventsUseCase,
                           GetEventDetailsUseCase getEventDetailsUseCase,
                           CreateEventUseCase createEventUseCase,
                           UpdateEventUseCase updateEventUseCase,
                           DeleteEventUseCase deleteEventUseCase,
                           GetEventTicketsUseCase getEventTicketsUseCase) {
        this.getEventsUseCase = getEventsUseCase;
        this.getEventDetailsUseCase = getEventDetailsUseCase;
        this.createEventUseCase = createEventUseCase;
        this.updateEventUseCase = updateEventUseCase;
        this.deleteEventUseCase = deleteEventUseCase;
        this.getEventTicketsUseCase = getEventTicketsUseCase;
    }

    @Operation(summary = "Listar eventos", description = "Retorna la cartelera completa de eventos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cartelera obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public List<EventResponse> listEvents() {
        return getEventsUseCase.execute().stream()
            .map(EventResponse::fromDomain)
            .toList();
    }

    @Operation(summary = "Detalle de evento", description = "Retorna un evento por su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evento encontrado"),
        @ApiResponse(responseCode = "404", description = "Evento no existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public EventResponse getEvent(@PathVariable Long id) {
        return EventResponse.fromDomain(getEventDetailsUseCase.execute(id));
    }

    @Operation(summary = "Crear evento", description = "Registra un nuevo evento con su capacidad inicial")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Evento creado"),
        @ApiResponse(responseCode = "400", description = "Payload invalido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody CreateEventRequest request) {
        var event = createEventUseCase.execute(request.cityId(), request.name(), request.venue(), request.capacity());
        return ResponseEntity.status(HttpStatus.CREATED).body(EventResponse.fromDomain(event));
    }

    @Operation(summary = "Actualizar evento", description = "Actualiza nombre, lugar y capacidad de un evento")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evento actualizado"),
        @ApiResponse(responseCode = "404", description = "Evento no existe"),
        @ApiResponse(responseCode = "400", description = "Payload invalido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public EventResponse updateEvent(@PathVariable Long id, @Valid @RequestBody UpdateEventRequest request) {
        return EventResponse.fromDomain(
            updateEventUseCase.execute(id, request.name(), request.venue(), request.capacity()));
    }

    @Operation(summary = "Eliminar evento", description = "Elimina un evento que no tenga ventas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Evento eliminado"),
        @ApiResponse(responseCode = "404", description = "Evento no existe"),
        @ApiResponse(responseCode = "409", description = "Evento con ventas activas"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        deleteEventUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Entradas de un evento", description = "Lista todas las entradas vendidas de un evento")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de entradas"),
        @ApiResponse(responseCode = "404", description = "Evento no existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}/tickets")
    public List<TicketResponseDto> getEventTickets(@PathVariable Long id) {
        return getEventTicketsUseCase.execute(id).stream()
            .map(TicketResponseDto::fromDomain)
            .toList();
    }
}
