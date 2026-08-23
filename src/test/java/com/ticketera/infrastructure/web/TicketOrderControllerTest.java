package com.ticketera.infrastructure.web;

import com.ticketera.application.usecase.OrderResult;
import com.ticketera.application.usecase.ProcessOrderUseCase;
import com.ticketera.application.usecase.SendBookingConfirmationUseCase;
import com.ticketera.domain.exception.SoldOutException;
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

@WebMvcTest(TicketOrderController.class)
class TicketOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProcessOrderUseCase processOrderUseCase;

    @MockitoBean
    private SendBookingConfirmationUseCase sendBookingConfirmationUseCase;

    @Test
    void purchasesTicketsAndReturns201() throws Exception {
        when(processOrderUseCase.execute("evt-1", 2))
            .thenReturn(new OrderResult("evt-1", "Jazz Night", 2, 98));

        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"eventId": "evt-1", "quantity": 2, "customerEmail": "customer@email.com"}
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.eventId").value("evt-1"))
            .andExpect(jsonPath("$.eventName").value("Jazz Night"))
            .andExpect(jsonPath("$.ticketsPurchased").value(2))
            .andExpect(jsonPath("$.remainingTickets").value(98));

        verify(sendBookingConfirmationUseCase).execute("customer@email.com", "Jazz Night");
    }

    @Test
    void purchasesWithoutOptionalEmailSkipsConfirmation() throws Exception {
        when(processOrderUseCase.execute("evt-1", 2))
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
    void returns422WhenSoldOut() throws Exception {
        when(processOrderUseCase.execute(any(), anyInt()))
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