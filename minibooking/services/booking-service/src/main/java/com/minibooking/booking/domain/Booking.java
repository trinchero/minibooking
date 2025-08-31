package com.minibooking.booking.domain;

import com.minibooking.common.BookingRequested;
import java.time.OffsetDateTime;
import java.util.UUID;

public class Booking {
    private final UUID id;
    private final String userId, resourceType, resourceRef;
    private final OffsetDateTime from,to;
    private final Double amount; private final String currency;
    private String status = "REQUESTED";

    private Booking(UUID id,String userId,String resourceType,String resourceRef,
                    OffsetDateTime from,OffsetDateTime to,Double amount,String currency) {
        if (from.isAfter(to)) throw new IllegalArgumentException("slotFrom > slotTo");
        this.id=id; this.userId=userId; this.resourceType=resourceType; this.resourceRef=resourceRef;
        this.from=from; this.to=to; this.amount=amount; this.currency=currency;
    }
    public static Booking request(CreateBooking c){
        return new Booking(UUID.randomUUID(), c.userId(), c.resourceType(), c.resourceRef(),
                c.slotFrom(), c.slotTo(), c.amount(), c.currency());
    }
    public UUID id(){ return id; }
    public BookingRequested toEvent(){
        return new BookingRequested(id.toString(), userId, resourceType, resourceRef,
                from.toString(), to.toString(), amount, currency, OffsetDateTime.now().toString());
    }
    public void apply(String s){ this.status = s; }
}
