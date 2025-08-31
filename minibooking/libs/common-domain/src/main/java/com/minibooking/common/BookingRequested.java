package com.minibooking.common;

public record BookingRequested(
        String bookingId,String userId,String resourceType,String resourceRef,
        String slotFrom,String slotTo,Double amount,String currency,String ts) {}