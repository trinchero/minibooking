package com.minibooking.orchestrator.adapters;

import com.minibooking.common.*;
import com.minibooking.kafka.JsonSerde;
import com.minibooking.orchestrator.domain.BookingDecider;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import java.time.Duration;

@Configuration
public class TopologyConfig {
    @Value("${topics.bookingRequested}") String bookingRequested;
    @Value("${topics.resourceEvents}") String resourceEvents;
    @Value("${topics.bookingLifecycle}") String bookingLifecycle;

    @Bean
    public Topology topology(){
        var b = new StreamsBuilder();
        var brSerde = new JsonSerde<>(BookingRequested.class);
        var reSerde = new JsonSerde<>(ResourceEvent.class);
        var lcSerde = new JsonSerde<>(BookingLifecycle.class);

        var br = b.stream(bookingRequested, Consumed.with(Serdes.String(), brSerde)).selectKey((k,v)->v.bookingId());
        var re = b.stream(resourceEvents, Consumed.with(Serdes.String(), reSerde)).selectKey((k,v)->v.bookingId());

        var joined = br.leftJoin(
                re,
                (brv, rev) -> BookingDecider.decide(brv, rev),
                JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofMinutes(15)),
                StreamJoined.with(Serdes.String(), brSerde, reSerde)
        );

        joined.to(bookingLifecycle, Produced.with(Serdes.String(), lcSerde));
        return b.build();
    }
}
