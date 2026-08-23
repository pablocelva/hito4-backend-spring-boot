package com.ticketera.infrastructure.notification;

import com.ticketera.application.port.MessageNotifier;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationService implements MessageNotifier {
    @Override
    public void send(String destination, String message) {
        System.out.println("Email to " + destination + ": " + message);
    }
}