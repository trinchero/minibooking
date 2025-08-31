package com.minibooking.common;

public record BookingRequested(
        String bookingId,String userId,String resourceType,String resourceRef,
        String slotFrom,String slotTo,Double amount,String currency,String ts) {}

public record ResourceEvent(
        String bookingId,String resourceRef,String status,String reason,String ts) {}

public record BookingLifecycle(
        String bookingId,String status,String ts) {}
