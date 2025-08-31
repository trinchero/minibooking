package com.minibooking.booking.adapters.out.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Document("booking")
public class BookingDoc {
    @Id public UUID id;
    public String userId, resourceType, resourceRef;
    public String slotFrom, slotTo;
    public Double amount; public String currency; public String status;
    public String createdAt = OffsetDateTime.now().toString();
    public String updatedAt = OffsetDateTime.now().toString();
}

@Document("outbox_event")
@CompoundIndexes({
        @CompoundIndex(name="idx_status_next", def="{ 'status': 1, 'nextAttemptAt': 1 }")
})
class OutboxEventDoc {
    @Id public UUID id;
    public String aggregateType, aggregateId, type;
    public Map<String,String> headers;
    public String payload;
    public String status = "NEW";           // NEW | PROCESSING | PUBLISHED | FAILED
    public int attempts = 0;
    public String nextAttemptAt = OffsetDateTime.now().toString();
    public String createdAt = OffsetDateTime.now().toString();
}
