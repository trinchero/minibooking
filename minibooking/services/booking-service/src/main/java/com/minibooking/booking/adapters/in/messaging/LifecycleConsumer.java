package com.minibooking.booking.adapters.in.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minibooking.booking.application.service.BookingAppService;
import com.minibooking.common.BookingLifecycle;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class LifecycleConsumer {
    private final BookingAppService app; private final ObjectMapper om=new ObjectMapper();
    public LifecycleConsumer(BookingAppService app){ this.app=app; }

    @KafkaListener(topics="${topics.bookingLifecycle}", groupId="booking-lifecycle")
    public void onMsg(String value){
        try {
            var e = om.readValue(value, BookingLifecycle.class);
            app.applyLifecycle(UUID.fromString(e.bookingId()), e.status());
        } catch(Exception ignored){}
    }
}
