package com.minibooking.booking.domain;
import java.time.OffsetDateTime;
public record CreateBooking(String userId,String resourceType,String resourceRef,
                            OffsetDateTime slotFrom,OffsetDateTime slotTo,Double amount,String currency) {}
