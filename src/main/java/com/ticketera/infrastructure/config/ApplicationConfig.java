package com.ticketera.infrastructure.config;

import com.ticketera.application.port.MessageNotifier;
import com.ticketera.application.usecase.CreateEventUseCase;
import com.ticketera.application.usecase.DeleteEventUseCase;
import com.ticketera.application.usecase.GetEventDetailsUseCase;
import com.ticketera.application.usecase.GetEventTicketsUseCase;
import com.ticketera.application.usecase.GetEventsUseCase;
import com.ticketera.application.usecase.ProcessOrderUseCase;
import com.ticketera.application.usecase.SendBookingConfirmationUseCase;
import com.ticketera.application.usecase.UpdateEventUseCase;
import com.ticketera.application.usecase.CreateCityUseCase;
import com.ticketera.application.usecase.DeleteCityUseCase;
import com.ticketera.application.usecase.GetCitiesUseCase;
import com.ticketera.application.usecase.GetCityDetailsUseCase;
import com.ticketera.application.usecase.UpdateCityUseCase;
import com.ticketera.domain.repository.CityRepository;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.repository.TicketRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public ProcessOrderUseCase processOrderUseCase(EventRepository eventRepository, TicketRepository ticketRepository, MessageNotifier notifier) {
        return new ProcessOrderUseCase(eventRepository, ticketRepository, notifier);
    }

    @Bean
    public CreateEventUseCase createEventUseCase(EventRepository repository) {
        return new CreateEventUseCase(repository);
    }

    @Bean
    public GetEventsUseCase getEventsUseCase(EventRepository repository) {
        return new GetEventsUseCase(repository);
    }

    @Bean
    public GetEventDetailsUseCase getEventDetailsUseCase(EventRepository repository) {
        return new GetEventDetailsUseCase(repository);
    }

    @Bean
    public UpdateEventUseCase updateEventUseCase(EventRepository repository) {
        return new UpdateEventUseCase(repository);
    }

    @Bean
    public DeleteEventUseCase deleteEventUseCase(EventRepository repository) {
        return new DeleteEventUseCase(repository);
    }

    @Bean
    public GetEventTicketsUseCase getEventTicketsUseCase(TicketRepository ticketRepository) {
        return new GetEventTicketsUseCase(ticketRepository);
    }

    @Bean
    public SendBookingConfirmationUseCase sendBookingConfirmationUseCase(MessageNotifier notifier) {
        return new SendBookingConfirmationUseCase(notifier);
    }

    @Bean
    public CreateCityUseCase createCityUseCase(CityRepository repository) {
        return new CreateCityUseCase(repository);
    }

    @Bean
    public GetCitiesUseCase getCitiesUseCase(CityRepository repository) {
        return new GetCitiesUseCase(repository);
    }

    @Bean
    public GetCityDetailsUseCase getCityDetailsUseCase(CityRepository repository) {
        return new GetCityDetailsUseCase(repository);
    }

    @Bean
    public UpdateCityUseCase updateCityUseCase(CityRepository repository) {
        return new UpdateCityUseCase(repository);
    }

    @Bean
    public DeleteCityUseCase deleteCityUseCase(CityRepository repository) {
        return new DeleteCityUseCase(repository);
    }
}
