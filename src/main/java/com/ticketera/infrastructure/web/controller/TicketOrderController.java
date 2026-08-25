package com.ticketera.infrastructure.web.controller;

import com.ticketera.application.usecase.ProcessOrderUseCase;
import com.ticketera.application.usecase.SendBookingConfirmationUseCase;
import com.ticketera.infrastructure.web.dto.OrderResponse;
import com.ticketera.infrastructure.web.dto.TicketOrderRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Orders", description = "Compra de entradas con validacion de inventario")
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

    @Operation(summary = "Comprar entradas", description = "Procesa una orden, descuenta inventario y confirma por email si se indica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Compra procesada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos"),
        @ApiResponse(responseCode = "404", description = "Evento no existe"),
        @ApiResponse(responseCode = "422", description = "Entradas insuficientes"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<OrderResponse> purchase(@Valid @RequestBody TicketOrderRequest request) {
        var result = processOrderUseCase.execute(
            request.eventId(), request.quantity(), request.customerName(), request.customerEmail());

        if (request.customerEmail() != null && !request.customerEmail().isBlank()) {
            sendBookingConfirmationUseCase.execute(request.customerEmail(), result.eventName());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse.fromDomain(result));
    }
}