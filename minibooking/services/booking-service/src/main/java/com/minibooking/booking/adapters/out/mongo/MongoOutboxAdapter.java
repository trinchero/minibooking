package com.minibooking.booking.adapters.out.mongo;

import com.minibooking.booking.application.ports.OutboxPort;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
public class MongoOutboxAdapter implements OutboxPort {
    private final OutboxMongoRepo repo;
    public MongoOutboxAdapter(OutboxMongoRepo r){ this.repo=r; }
    public void save(String aggregateId, String type, String payload, Map<String,String> headers){
        var e = new OutboxEventDoc();
        e.id = UUID.randomUUID();
        e.aggregateType = "booking";
        e.aggregateId = aggregateId;
        e.type = type;
        e.payload = payload;
        e.headers = headers;
        repo.save(e);
    }
}
