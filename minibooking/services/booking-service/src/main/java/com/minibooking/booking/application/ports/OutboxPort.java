package com.minibooking.booking.application.ports;
import java.util.Map;

public interface OutboxPort {
    void save(String aggregateId, String type, String payload, Map<String,String> headers);
}
