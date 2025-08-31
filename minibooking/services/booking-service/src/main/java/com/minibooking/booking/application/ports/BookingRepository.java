package com.minibooking.booking.application.ports;
import com.minibooking.booking.domain.Booking;
import java.util.*;

public interface BookingRepository {
    void save(Booking b);
    Optional<Booking> find(UUID id);
    void updateStatus(UUID id, String status);
}
