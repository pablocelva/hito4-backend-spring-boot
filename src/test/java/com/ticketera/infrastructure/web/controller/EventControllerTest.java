package com.ticketera.infrastructure.web.controller;

import com.ticketera.application.usecase.CreateEventUseCase;
import com.ticketera.application.usecase.DeleteEventUseCase;
import com.ticketera.application.usecase.GetEventDetailsUseCase;
import com.ticketera.application.usecase.GetEventTicketsUseCase;
import com.ticketera.application.usecase.GetEventsUseCase;
import com.ticketera.application.usecase.UpdateEventUseCase;
import com.ticketera.domain.entity.Event;
import com.ticketera.domain.exception.EventNotFoundException;
import com.ticketera.domain.valueobject.EventId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Event Controller")
@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetEventsUseCase getEventsUseCase;

    @MockitoBean
    private GetEventDetailsUseCase getEventDetailsUseCase;

    @MockitoBean
    private CreateEventUseCase createEventUseCase;

    @MockitoBean
    private UpdateEventUseCase updateEventUseCase;

    @MockitoBean
    private DeleteEventUseCase deleteEventUseCase;

    @MockitoBean
    private GetEventTicketsUseCase getEventTicketsUseCase;

    @Test
    @DisplayName("Lists all events")
    void listsAllEvents() throws Exception {
        when(getEventsUseCase.execute()).thenReturn(List.of(
            Event.reconstitute(new EventId("evt-1"), "Jazz Night", "Teatro", 100, 90)));

        mockMvc.perform(get("/api/v1/events"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value("evt-1"))
            .andExpect(jsonPath("$[0].cityId").value("LIM"))
            .andExpect(jsonPath("$[0].name").value("Jazz Night"))
            .andExpect(jsonPath("$[0].availableTickets").value(90))
            .andExpect(jsonPath("$[0].ticketsSold").value(10));
    }

    @Test
    @DisplayName("Returns event by id")
    void returnsEventById() throws Exception {
        when(getEventDetailsUseCase.execute("evt-1"))
            .thenReturn(Event.reconstitute(new EventId("evt-1"), "Jazz Night", "Teatro", 100, 90));

        mockMvc.perform(get("/api/v1/events/evt-1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("evt-1"))
            .andExpect(jsonPath("$.cityId").value("LIM"));
    }

    @Test
    @DisplayName("Returns 404 when event not found")
    void returns404WhenEventNotFound() throws Exception {
        when(getEventDetailsUseCase.execute("missing"))
            .thenThrow(new EventNotFoundException("Event not found: missing"));

        mockMvc.perform(get("/api/v1/events/missing"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value(404))
            .andExpect(jsonPath("$.message").value("Event not found: missing"))
            .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Creates event and returns 201")
    void createsEventAndReturns201() throws Exception {
        when(createEventUseCase.execute(any(), any(), anyInt()))
            .thenReturn(Event.reconstitute(new EventId("evt-new"), "Rock Fest", "Estadio", 1000, 1000));

        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"cityId": "LIM", "name": "Rock Fest", "venue": "Estadio", "capacity": 1000}
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value("evt-new"))
            .andExpect(jsonPath("$.cityId").value("LIM"))
            .andExpect(jsonPath("$.availableTickets").value(1000));
    }

    @Test
    @DisplayName("Returns 400 when create body is invalid")
    void returns400WhenCreateBodyIsInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"cityId": "", "name": "", "venue": "", "capacity": -5}
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    @DisplayName("Updates event and returns 200")
    void updatesEventAndReturns200() throws Exception {
        when(updateEventUseCase.execute("evt-1", "Rock Night", "Estadio", 500))
            .thenReturn(Event.reconstitute(new EventId("evt-1"), "Rock Night", "Estadio", 500, 500));

        mockMvc.perform(put("/api/v1/events/evt-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name": "Rock Night", "venue": "Estadio", "capacity": 500}
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("evt-1"))
            .andExpect(jsonPath("$.name").value("Rock Night"))
            .andExpect(jsonPath("$.availableTickets").value(500));
    }

    @Test
    @DisplayName("Returns 404 when updating non-existent event")
    void returns404WhenUpdatingNonExistentEvent() throws Exception {
        when(updateEventUseCase.execute(anyString(), anyString(), anyString(), anyInt()))
            .thenThrow(new EventNotFoundException("Event not found: missing"));

        mockMvc.perform(put("/api/v1/events/missing")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name": "Rock Night", "venue": "Estadio", "capacity": 500}
                    """))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    @DisplayName("Deletes event and returns 204")
    void deletesEventAndReturns204() throws Exception {
        doNothing().when(deleteEventUseCase).execute("evt-1");

        mockMvc.perform(delete("/api/v1/events/evt-1"))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Returns 404 when deleting non-existent event")
    void returns404WhenDeletingNonExistentEvent() throws Exception {
        org.mockito.Mockito.doThrow(new EventNotFoundException("Event not found: missing"))
            .when(deleteEventUseCase).execute("missing");

        mockMvc.perform(delete("/api/v1/events/missing"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value(404));
    }
}
