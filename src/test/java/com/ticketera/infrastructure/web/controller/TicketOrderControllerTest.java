package com.ticketera.infrastructure.web.controller;

import com.ticketera.application.usecase.OrderResult;
import com.ticketera.application.usecase.ProcessOrderUseCase;
import com.ticketera.application.usecase.SendBookingConfirmationUseCase;
import com.ticketera.domain.exception.SoldOutException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Ticket Order Controller")
@WebMvcTest(TicketOrderController.class)
class TicketOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProcessOrderUseCase processOrderUseCase;

    @MockitoBean
    private SendBookingConfirmationUseCase sendBookingConfirmationUseCase;

    @Test
    @DisplayName("Purchases tickets and returns 201")
    void purchasesTicketsAndReturns201() throws Exception {
        when(processOrderUseCase.execute(eq("evt-1"), eq(2), anyString(), anyString()))
            .thenReturn(new OrderResult("evt-1", "Jazz Night", 2, 98));

        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"eventId": "evt-1", "quantity": 2, "customerName": "Juan", "customerEmail": "customer@email.com"}
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.eventId").value("evt-1"))
            .andExpect(jsonPath("$.eventName").value("Jazz Night"))
            .andExpect(jsonPath("$.ticketsPurchased").value(2))
            .andExpect(jsonPath("$.remainingTickets").value(98));

        verify(sendBookingConfirmationUseCase).execute("customer@email.com", "Jazz Night");
    }

    @Test
    @DisplayName("Purchases without optional email skips confirmation")
    void purchasesWithoutOptionalEmailSkipsConfirmation() throws Exception {
        when(processOrderUseCase.execute(eq("evt-1"), eq(2), any(), any()))
            .thenReturn(new OrderResult("evt-1", "Jazz Night", 2, 98));

        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"eventId": "evt-1", "quantity": 2}
                    """))
            .andExpect(status().isCreated());

        verify(sendBookingConfirmationUseCase, never()).execute(any(), any());
    }

    @Test
    @DisplayName("Returns 422 when sold out")
    void returns422WhenSoldOut() throws Exception {
        when(processOrderUseCase.execute(any(), anyInt(), any(), any()))
            .thenThrow(new SoldOutException("Not enough tickets available"));

        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"eventId": "evt-1", "quantity": 600}
                    """))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.code").value(422))
            .andExpect(jsonPath("$.message").value("Not enough tickets available"));
    }

    @Test
    @DisplayName("Returns 400 when request is invalid")
    void returns400WhenRequestIsInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"eventId": "", "quantity": 0}
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(400));
    }
}
