package com.ticketera.infrastructure.config;

import com.ticketera.domain.entity.City;
import com.ticketera.domain.entity.Event;
import com.ticketera.domain.repository.CityRepository;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.valueobject.CityId;
import com.ticketera.domain.valueobject.EventId;
import com.ticketera.domain.valueobject.TicketQuantity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevDataSeeder {

    @Bean
    CommandLineRunner seedData(CityRepository cityRepository, EventRepository eventRepository) {
        return args -> {
            if (cityRepository.findAll().isEmpty()) {
                var lim = new City(new CityId("LIM"), "Lima");
                cityRepository.save(lim);
            }

            if (eventRepository.findAll().isEmpty()) {
                var jazz = new Event(
                    new EventId("evt-jazz-001"), "Jazz Night", "Gran Teatro Lima", 500);
                var rock = new Event(
                    new EventId("evt-rock-002"), "Rock Fest", "Estadio Nacional", 5000);
                rock.reserveTickets(new TicketQuantity(1200));
                eventRepository.save(jazz);
                eventRepository.save(rock);
            }
        };
    }
}
