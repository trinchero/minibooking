package com.minibooking.common;

public record ResourceEvent(
        String bookingId,String resourceRef,String status,String reason,String ts) {}
