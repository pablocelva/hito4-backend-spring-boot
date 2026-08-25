package com.ticketera.infrastructure.web.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/healthcheck")
    public Map<String, Object> healthcheck() {
        return Map.of(
            "status", "UP",
            "app", "ticketera",
            "timestamp", LocalDateTime.now().toString());
    }
}