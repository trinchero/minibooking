package com.minibooking.resource.domain;
public class ReservationPolicy {
    public static boolean canReserve(String resourceRef){ return resourceRef==null || !resourceRef.endsWith("X"); }
}
