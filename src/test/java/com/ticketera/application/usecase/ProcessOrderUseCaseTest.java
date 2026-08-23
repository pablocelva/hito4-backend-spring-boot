package com.ticketera.application.usecase;

import com.ticketera.application.port.MessageNotifier ;
import com.ticketera.domain.entity.Event;
import com.ticketera.domain.exception.InvalidOrderException;
import com.ticketera.domain.repository.EventRepository;
import com.ticketera.domain.valueobject.EventId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Process Order Use Case")
public class ProcessOrderUseCaseTest {
    @Test
    @DisplayName("Should fail when eventId is null")
    public void shouldFailWhenEventIdIsNull() {
        EventRepository repositoryMock = mock(EventRepository.class);
        MessageNotifier  notifierMock = mock(MessageNotifier .class);
        ProcessOrderUseCase useCase = new ProcessOrderUseCase(repositoryMock, notifierMock);

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(null, 2));
    }

    @Test
    @DisplayName("Should fail when eventId is empty")
    public void shouldFailWhenEventIdIsEmpty() {
        EventRepository repositoryMock = mock(EventRepository.class);
        MessageNotifier  notifierMock = mock(MessageNotifier .class);
        ProcessOrderUseCase useCase = new ProcessOrderUseCase(repositoryMock, notifierMock);

        assertThrows(IllegalArgumentException.class, () -> useCase.execute("", 2));
    }

    @Test
    @DisplayName("Should fail when quantity is not positive")
    public void shouldFailWhenQuantityIsNotPositive() {
        EventRepository repositoryMock = mock(EventRepository.class);
        MessageNotifier  notifierMock = mock(MessageNotifier .class);
        ProcessOrderUseCase useCase = new ProcessOrderUseCase(repositoryMock, notifierMock);

        assertThrows(InvalidOrderException.class, () -> useCase.execute("EVT-001", 0));
    }

    @Test
    @DisplayName("Should fail when event not found")
    public void shouldFailWhenEventNotFound() {
        EventRepository repositoryMock = mock(EventRepository.class);
        MessageNotifier  notifierMock = mock(MessageNotifier .class);
        ProcessOrderUseCase useCase = new ProcessOrderUseCase(repositoryMock, notifierMock);
        when(repositoryMock.findById(any(EventId.class))).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> useCase.execute("EVT-999", 2));
    }

    @Test
    @DisplayName("Should process order and reserve tickets successfully")
    public void shouldProcessOrderSuccessfully() {
        EventRepository repositoryMock = mock(EventRepository.class);
        MessageNotifier  notifierMock = mock(MessageNotifier .class);
        ProcessOrderUseCase useCase = new ProcessOrderUseCase(repositoryMock, notifierMock);
        Event event = new Event(new EventId("EVT-001"), "Jazz Night", "Jazz Club", 500);
        when(repositoryMock.findById(new EventId("EVT-001"))).thenReturn(Optional.of(event));

        useCase.execute("EVT-001", 2);

        verify(notifierMock, times(1)).send("admin@ticketera.com",
                "Order processed for: Jazz Night (2 tickets), with ID: EVT-001");
        assertEquals(498, event.getAvailableTickets());
    }
}