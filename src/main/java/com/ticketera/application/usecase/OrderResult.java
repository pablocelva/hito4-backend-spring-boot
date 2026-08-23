package com.ticketera.application.usecase;

public record OrderResult(String eventId, String eventName, int ticketsPurchased, int remainingTickets) {
}