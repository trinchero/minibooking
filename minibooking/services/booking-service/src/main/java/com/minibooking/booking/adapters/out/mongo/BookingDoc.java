package com.minibooking.booking.adapters.out.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;
import java.util.UUID;

@Document("booking")
public class BookingDoc {
    @Id
    public UUID id;
    public String userId, resourceType, resourceRef;
    public String slotFrom, slotTo;
    public Double amount;
    public String currency;
    public String status;
    public String createdAt = OffsetDateTime.now().toString();
    public String updatedAt = OffsetDateTime.now().toString();
}
