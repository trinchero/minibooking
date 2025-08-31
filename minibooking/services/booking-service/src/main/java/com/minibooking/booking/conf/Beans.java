package com.minibooking.booking.conf;

import com.minibooking.booking.application.ports.*;
import com.minibooking.booking.application.service.BookingAppService;
import org.springframework.context.annotation.*;

@Configuration
public class Beans {
    @Bean BookingAppService bookingApp(BookingRepository repo, OutboxPort outbox){ return new BookingAppService(repo,outbox); }
}
