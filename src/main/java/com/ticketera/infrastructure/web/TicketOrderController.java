package com.ticketera.infrastructure.web;

import com.ticketera.application.usecase.ProcessOrderUseCase;
import com.ticketera.application.usecase.SendBookingConfirmationUseCase;
import com.ticketera.infrastructure.web.dto.OrderResponse;
import com.ticketera.infrastructure.web.dto.TicketOrderRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class TicketOrderController {

    private final ProcessOrderUseCase processOrderUseCase;
    private final SendBookingConfirmationUseCase sendBookingConfirmationUseCase;

    public TicketOrderController(ProcessOrderUseCase processOrderUseCase,
                                 SendBookingConfirmationUseCase sendBookingConfirmationUseCase) {
        this.processOrderUseCase = processOrderUseCase;
        this.sendBookingConfirmationUseCase = sendBookingConfirmationUseCase;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> purchase(@Valid @RequestBody TicketOrderRequest request) {
        var result = processOrderUseCase.execute(request.eventId(), request.quantity());

        if (request.customerEmail() != null && !request.customerEmail().isBlank()) {
            sendBookingConfirmationUseCase.execute(request.customerEmail(), result.eventName());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse.fromDomain(result));
    }
}