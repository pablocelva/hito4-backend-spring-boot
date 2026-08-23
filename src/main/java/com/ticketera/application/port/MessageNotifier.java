package com.ticketera.application.port;

public interface MessageNotifier {
    void send(String destination, String message);
}