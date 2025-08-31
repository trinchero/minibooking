package com.minibooking.resource.adapters.out;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minibooking.common.ResourceEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ResourceEventPublisher implements java.util.function.Consumer<ResourceEvent> {
    private final KafkaTemplate<String,String> kafka; private final ObjectMapper om=new ObjectMapper();
    @Value("${topics.resourceEvents}") String topic;
    public ResourceEventPublisher(KafkaTemplate<String,String> k){ this.kafka=k; }
    @Override public void accept(ResourceEvent e){
        try { kafka.send(topic, e.bookingId(), om.writeValueAsString(e)); } catch(Exception ex){ throw new RuntimeException(ex); }
    }
}
