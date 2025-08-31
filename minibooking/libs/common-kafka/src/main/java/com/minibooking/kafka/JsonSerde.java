package com.minibooking.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.*;

public class JsonSerde<T> implements Serde<T> {
    private final Serializer<T> ser; private final Deserializer<T> de;
    public JsonSerde(Class<T> type){
        var om=new ObjectMapper();
        ser=(topic,data)->{ try{ return om.writeValueAsBytes(data);}catch(Exception e){throw new RuntimeException(e);} };
        de=(topic,bytes)->{ try{ return bytes==null?null:om.readValue(bytes,type);}catch(Exception e){throw new RuntimeException(e);} };
    }
    public Serializer<T> serializer(){ return ser; }
    public Deserializer<T> deserializer(){ return de; }
}
