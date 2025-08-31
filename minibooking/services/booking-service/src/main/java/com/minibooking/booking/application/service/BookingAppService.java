package com.minibooking.booking.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minibooking.booking.application.ports.*;
import com.minibooking.booking.domain.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

public class BookingAppService {
    private final BookingRepository repo; private final OutboxPort outbox; private final ObjectMapper om = new ObjectMapper();
    @Value("${topics.bookingRequested}") String bookingRequestedTopic;

    public BookingAppService(BookingRepository r, OutboxPort o){ this.repo=r; this.outbox=o; }

    @Transactional
    public UUID create(CreateBooking cmd){
        var b = Booking.request(cmd);
        repo.save(b);
        try {
            var evt = b.toEvent();
            outbox.save(b.id().toString(), bookingRequestedTopic, om.writeValueAsString(evt),
                    Map.of("eventId", UUID.randomUUID().toString()));
        } catch(Exception e){ throw new RuntimeException(e); }
        return b.id();
    }

    @Transactional
    public void applyLifecycle(UUID id, String status){
        repo.updateStatus(id, status);
    }
}
