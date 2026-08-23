package com.ticketera.infrastructure.web.dto;

import java.time.LocalDateTime;

public record ErrorResponse(int code, String message, LocalDateTime timestamp) {

    public static ErrorResponse of(int code, String message) {
        return new ErrorResponse(code, message, LocalDateTime.now());
    }
}