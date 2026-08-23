package com.ticketera.infrastructure.config;

import com.ticketera.application.port.MessageNotifier;
import com.ticketera.application.usecase.CreateEventUseCase;
import com.ticketera.application.usecase.GetEventDetailsUseCase;
import com.ticketera.application.usecase.GetEventsUseCase;
import com.ticketera.application.usecase.ProcessOrderUseCase;
import com.ticketera.application.usecase.SendBookingConfirmationUseCase;

import com.ticketera.domain.repository.EventRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public ProcessOrderUseCase processOrderUseCase(EventRepository repository, MessageNotifier notifier) {
        return new ProcessOrderUseCase(repository, notifier);
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
    public SendBookingConfirmationUseCase sendBookingConfirmationUseCase(MessageNotifier notifier) {
        return new SendBookingConfirmationUseCase(notifier);
    }
}