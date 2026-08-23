package com.ticketera.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class OpenApiConfig {

    @Bean
    public OpenAPI ticketeraOpenAPI() {
        return new OpenAPI().info(new Info()
            .title("Ticketera API")
            .version("1.0")
            .description("Microservicio de venta de entradas - Cartelera y órdenes de compra"));
    }
}