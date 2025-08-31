package com.minibooking.booking.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;

@Configuration
public class MongoTxConfig {
    @Bean MongoTransactionManager tx(MongoDatabaseFactory f){ return new MongoTransactionManager(f); }
}
