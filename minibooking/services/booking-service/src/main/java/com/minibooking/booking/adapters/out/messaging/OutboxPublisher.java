package com.minibooking.booking.adapters.out.messaging;

import com.minibooking.booking.adapters.out.mongo.OutboxEventDoc;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
public class OutboxPublisher {
    private final MongoTemplate mongo; private final KafkaTemplate<String,String> kafka;
    public OutboxPublisher(MongoTemplate mongo, KafkaTemplate<String,String> kafka){ this.mongo=mongo; this.kafka=kafka; }

    @Scheduled(fixedDelayString="PT2S")
    public void publishBatch(){
        for(int i=0;i<100;i++){
            OutboxEventDoc e = claimOne();
            if(e==null) break;
            try {
                kafka.send(e.type, e.aggregateId, e.payload).get();
                markPublished(e.id);
            } catch(Exception ex){
                onFailure(e.id, e.attempts);
            }
        }
    }

    private OutboxEventDoc claimOne(){
        var q = new Query(new Criteria().andOperator(
                where("status").is("NEW"),
                where("nextAttemptAt").lte(OffsetDateTime.now().toString())
        )).with(Sort.by("createdAt").ascending()).limit(1);
        var u = new Update().set("status","PROCESSING");
        return mongo.findAndModify(q, u, FindAndModifyOptions.options().returnNew(true), OutboxEventDoc.class, "outbox_event");
    }
    private void markPublished(java.util.UUID id){
        mongo.updateFirst(Query.query(where("_id").is(id)), new Update().set("status","PUBLISHED"), "outbox_event");
    }
    private void onFailure(java.util.UUID id, int attempts){
        int next = attempts+1; long backoff = (long)Math.pow(2,next);
        mongo.updateFirst(Query.query(where("_id").is(id)),
                new Update().set("attempts",next).set("status", next>=10?"FAILED":"NEW").set("nextAttemptAt", OffsetDateTime.now().plusSeconds(backoff).toString()),
                "outbox_event");
    }
}
