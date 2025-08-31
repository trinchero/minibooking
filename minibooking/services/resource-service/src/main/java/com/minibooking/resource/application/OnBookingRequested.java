package com.minibooking.resource.application;

import com.minibooking.common.*;
import com.minibooking.resource.domain.ReservationPolicy;

public class OnBookingRequested {
    private final java.util.function.Consumer<ResourceEvent> publisher;
    public OnBookingRequested(java.util.function.Consumer<ResourceEvent> publisher){ this.publisher=publisher; }
    public void handle(BookingRequested br){
        boolean ok = ReservationPolicy.canReserve(br.resourceRef());
        var e = new ResourceEvent(br.bookingId(), br.resourceRef(), ok?"RESERVED":"REJECTED", ok?null:"NO_STOCK", java.time.OffsetDateTime.now().toString());
        publisher.accept(e);
    }
}
