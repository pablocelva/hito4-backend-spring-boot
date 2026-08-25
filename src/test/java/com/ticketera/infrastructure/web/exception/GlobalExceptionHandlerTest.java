package com.ticketera.infrastructure.web.exception;

import com.ticketera.domain.exception.*;
import com.ticketera.infrastructure.web.GlobalExceptionHandler;
import com.ticketera.infrastructure.web.controller.CityController;
import com.ticketera.infrastructure.web.controller.EventController;
import com.ticketera.infrastructure.web.controller.TicketOrderController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({GlobalExceptionHandler.class, CityController.class, EventController.class, TicketOrderController.class})
@DisplayName("Global Exception Handler")
class GlobalExceptionHandlerTest {

    @Autowired
    private org.springframework.test.web.servlet.MockMvc mockMvc;

    @MockitoBean
    private com.ticketera.application.usecase.GetEventsUseCase getEventsUseCase;

    @MockitoBean
    private com.ticketera.application.usecase.GetEventDetailsUseCase getEventDetailsUseCase;

    @MockitoBean
    private com.ticketera.application.usecase.CreateEventUseCase createEventUseCase;

    @MockitoBean
    private com.ticketera.application.usecase.UpdateEventUseCase updateEventUseCase;

    @MockitoBean
    private com.ticketera.application.usecase.DeleteEventUseCase deleteEventUseCase;

    @MockitoBean
    private com.ticketera.application.usecase.GetEventTicketsUseCase getEventTicketsUseCase;

    @MockitoBean
    private com.ticketera.application.usecase.CreateCityUseCase createCityUseCase;

    @MockitoBean
    private com.ticketera.application.usecase.GetCitiesUseCase getCitiesUseCase;

    @MockitoBean
    private com.ticketera.application.usecase.GetCityDetailsUseCase getCityDetailsUseCase;

    @MockitoBean
    private com.ticketera.application.usecase.UpdateCityUseCase updateCityUseCase;

    @MockitoBean
    private com.ticketera.application.usecase.DeleteCityUseCase deleteCityUseCase;

    @MockitoBean
    private com.ticketera.application.usecase.ProcessOrderUseCase processOrderUseCase;

    @MockitoBean
    private com.ticketera.application.usecase.SendBookingConfirmationUseCase sendBookingConfirmationUseCase;

    @Test
    @DisplayName("EventNotFoundException returns 404")
    void eventNotFoundReturns404() throws Exception {
        when(getEventDetailsUseCase.execute(999L))
            .thenThrow(new EventNotFoundException("Event not found: 999"));

        mockMvc.perform(get("/api/v1/events/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value(404))
            .andExpect(jsonPath("$.message").value("Event not found: 999"))
            .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("CityNotFoundException returns 404")
    void cityNotFoundReturns404() throws Exception {
        when(getCityDetailsUseCase.execute(999L))
            .thenThrow(new CityNotFoundException("City not found: 999"));

        mockMvc.perform(get("/api/v1/cities/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value(404))
            .andExpect(jsonPath("$.message").value("City not found: 999"));
    }

    @Test
    @DisplayName("SoldOutException returns 422")
    void soldOutReturns422() throws Exception {
        when(processOrderUseCase.execute(1L, 10, "Juan", "juan@test.com"))
            .thenThrow(new SoldOutException("Insufficient tickets"));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"eventId": 1, "quantity": 10, "customerName": "Juan", "customerEmail": "juan@test.com"}
                    """))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.code").value(422))
            .andExpect(jsonPath("$.message").value("Insufficient tickets"));
    }

    @Test
    @DisplayName("IllegalStateException returns 409")
    void illegalStateReturns409() throws Exception {
        when(processOrderUseCase.execute(1L, 1, "Juan", "juan@test.com"))
            .thenThrow(new IllegalStateException("Conflict detected"));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"eventId": 1, "quantity": 1, "customerName": "Juan", "customerEmail": "juan@test.com"}
                    """))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.code").value(409))
            .andExpect(jsonPath("$.message").value("Conflict detected"));
    }

    @Test
    @DisplayName("IllegalArgumentException returns 400")
    void illegalArgumentReturns400() throws Exception {
        when(getEventDetailsUseCase.execute(0L))
            .thenThrow(new IllegalArgumentException("Invalid event id format"));

        mockMvc.perform(get("/api/v1/events/0"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400))
            .andExpect(jsonPath("$.message").value("Invalid event id format"));
    }

    @Test
    @DisplayName("Generic exception returns 500")
    void genericExceptionReturns500() throws Exception {
        when(getEventDetailsUseCase.execute(anyLong()))
            .thenThrow(new RuntimeException("Unexpected failure"));

        mockMvc.perform(get("/api/v1/events/999"))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.code").value(500))
            .andExpect(jsonPath("$.message").value("Internal server error"));
    }
}
