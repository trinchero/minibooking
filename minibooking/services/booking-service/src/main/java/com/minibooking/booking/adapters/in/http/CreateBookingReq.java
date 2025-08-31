package com.minibooking.booking.adapters.in.http;

import com.minibooking.booking.domain.CreateBooking;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

record CreateBookingReq(@NotBlank String userId, @NotBlank String resourceType, String resourceRef,
                        @NotNull OffsetDateTime slotFrom, @NotNull OffsetDateTime slotTo,
                        Double amount, String currency) {
    CreateBooking toCmd(){ return new CreateBooking(userId,resourceType,resourceRef,slotFrom,slotTo,amount,currency); }
}