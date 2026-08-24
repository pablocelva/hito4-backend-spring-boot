package com.ticketera.infrastructure.web.controller;

import com.ticketera.application.usecase.CreateEventUseCase;
import com.ticketera.application.usecase.GetEventDetailsUseCase;
import com.ticketera.application.usecase.GetEventsUseCase;
import com.ticketera.domain.entity.Event;
import com.ticketera.domain.exception.EventNotFoundException;
import com.ticketera.domain.valueobject.EventId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Test
    void listsAllEvents() throws Exception {
        when(getEventsUseCase.execute()).thenReturn(List.of(
            Event.reconstitute(new EventId("evt-1"), "Jazz Night", "Teatro", 100, 90)));

        mockMvc.perform(get("/api/v1/events"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value("evt-1"))
            .andExpect(jsonPath("$[0].name").value("Jazz Night"))
            .andExpect(jsonPath("$[0].availableTickets").value(90))
            .andExpect(jsonPath("$[0].ticketsSold").value(10));
    }

    @Test
    void returnsEventById() throws Exception {
        when(getEventDetailsUseCase.execute("evt-1"))
            .thenReturn(Event.reconstitute(new EventId("evt-1"), "Jazz Night", "Teatro", 100, 90));

        mockMvc.perform(get("/api/v1/events/evt-1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("evt-1"));
    }

    @Test
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
    void createsEventAndReturns201() throws Exception {
        when(createEventUseCase.execute(any(), any(), anyInt()))
            .thenReturn(Event.reconstitute(new EventId("evt-new"), "Rock Fest", "Estadio", 1000, 1000));

        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name": "Rock Fest", "venue": "Estadio", "capacity": 1000}
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value("evt-new"))
            .andExpect(jsonPath("$.availableTickets").value(1000));
    }

    @Test
    void returns400WhenCreateBodyIsInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"name": "", "venue": "", "capacity": -5}
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400));
    }
}