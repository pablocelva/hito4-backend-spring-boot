package com.ticketera.infrastructure.config;

import com.ticketera.domain.entity.Event;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.valueobject.TicketQuantity;
import com.ticketera.domain.valueobject.EventId;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevDataSeeder {

    @Bean
    CommandLineRunner seedEvents(EventRepository repository) {
        return args -> {
            if (repository.findAll().isEmpty()) {
                var jazz = new Event(
                    new EventId("evt-jazz-001"), "Jazz Night", "Gran Teatro Lima", 500);
                var rock = new Event(
                    new EventId("evt-rock-002"), "Rock Fest", "Estadio Nacional", 5000);
                rock.reserveTickets(new TicketQuantity(1200));
                repository.save(jazz);
                repository.save(rock);
            }
        };
    }
}