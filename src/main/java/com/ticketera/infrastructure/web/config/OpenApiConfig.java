package com.ticketera.infrastructure.web.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class OpenApiConfig {

    @Bean
    public OpenAPI ticketeraOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Ticketera API")
                .version("1.0")
                .description("Documentacion de API Microservicio de venta de entradas - Cartelera y ordenes de compra")
                .contact(new Contact()
                    .name("Ticketera Team"))
                .license(new License()
                    .name("MIT License")));
    }
}