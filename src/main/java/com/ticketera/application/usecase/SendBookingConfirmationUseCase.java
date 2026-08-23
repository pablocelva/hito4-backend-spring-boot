package com.ticketera.application.usecase;

import com.ticketera.application.port.MessageNotifier ;
import com.ticketera.domain.valueobject.Email;

public class SendBookingConfirmationUseCase {
    private final MessageNotifier  notifier;

    public SendBookingConfirmationUseCase(MessageNotifier  notifier) {
        this.notifier = notifier;
    }

    public void execute(String customerEmail, String eventName) {
        Email email = new Email(customerEmail);
        notifier.send(customerEmail, "Booking confirmed for: " + eventName);
    }
}
