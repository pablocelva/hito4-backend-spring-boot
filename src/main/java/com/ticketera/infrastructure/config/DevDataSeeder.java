package com.ticketera.infrastructure.config;

import com.ticketera.domain.entity.City;
import com.ticketera.domain.entity.Event;
import com.ticketera.domain.repository.CityRepository;
import com.ticketera.domain.repository.EventRepository;
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
                var lim = new City(null, "LIM", "Lima");
                var bog = new City(null, "BOG", "Bogota");
                var mad = new City(null, "MAD", "Madrid");
                cityRepository.save(lim);
                cityRepository.save(bog);
                cityRepository.save(mad);
            }

            if (eventRepository.findAll().isEmpty()) {
                var limaId = cityRepository.findByCode("LIM").map(c -> c.getId().value()).orElse(1L);
                var jazz = new Event(
                    "evt-jazz-001", "Jazz Night", "Gran Teatro Lima", 500);
                jazz.setCityId(limaId);

                var rock = new Event(
                    "evt-rock-002", "Rock Fest", "Estadio Nacional", 5000);
                rock.setCityId(limaId);
                rock.reserveTickets(new TicketQuantity(1200));

                eventRepository.save(jazz);
                eventRepository.save(rock);
            }
        };
    }
}
