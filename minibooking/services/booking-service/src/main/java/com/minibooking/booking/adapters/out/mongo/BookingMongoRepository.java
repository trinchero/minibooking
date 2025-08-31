package com.minibooking.booking.adapters.out.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookingMongoRepository extends MongoRepository<BookingDoc, UUID> {
}
