package com.minibooking.resource.adapters.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minibooking.common.BookingRequested;
import com.minibooking.resource.application.OnBookingRequested;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BookingRequestedListener {
    private final ObjectMapper om = new ObjectMapper(); private final OnBookingRequested app;
    public BookingRequestedListener(OnBookingRequested app){ this.app = app; }

    @KafkaListener(topics="${topics.bookingRequested}", groupId="resource-service")
    public void onMsg(String v){ try { app.handle(om.readValue(v, BookingRequested.class)); } catch(Exception ignored){} }
}
