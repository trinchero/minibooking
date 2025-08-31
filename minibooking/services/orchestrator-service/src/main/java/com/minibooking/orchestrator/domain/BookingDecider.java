package com.minibooking.orchestrator.domain;

import com.minibooking.common.*;

public class BookingDecider {
    public static BookingLifecycle decide(BookingRequested br, ResourceEvent re){
        String status = (re!=null && "RESERVED".equals(re.status())) ? "CONFIRMED" : "CANCELLED";
        return new BookingLifecycle(br.bookingId(), status, java.time.OffsetDateTime.now().toString());
    }
}
