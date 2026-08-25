package com.ticketera.infrastructure.web.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ApiResponse DTO")
class ApiResponseTest {

    @Test
    @DisplayName("Creates success response with name")
    void createsSuccessResponseWithName() {
        ApiResponse res = ApiResponse.ok("Creado", "evt-001");
        assertThat(res.status()).isEqualTo(200);
        assertThat(res.message()).isEqualTo("Creado");
        assertThat(res.name()).isEqualTo("evt-001");
        assertThat(res.timestamp()).isNotNull().isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    @DisplayName("Creates success response without name")
    void createsSuccessResponseWithoutName() {
        ApiResponse res = ApiResponse.ok("Listado");
        assertThat(res.status()).isEqualTo(200);
        assertThat(res.message()).isEqualTo("Listado");
        assertThat(res.name()).isNull();
        assertThat(res.timestamp()).isNotNull();
    }

    @Test
    @DisplayName("Creates error response")
    void createsErrorResponse() {
        ApiResponse res = ApiResponse.error(404, "No encontrado");
        assertThat(res.status()).isEqualTo(404);
        assertThat(res.message()).isEqualTo("No encontrado");
        assertThat(res.name()).isNull();
        assertThat(res.timestamp()).isNotNull();
    }

    @Test
    @DisplayName("Timestamp is recent")
    void timestampIsRecent() {
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        ApiResponse res = ApiResponse.ok("test");
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);
        assertThat(res.timestamp()).isAfter(before).isBefore(after);
    }
}