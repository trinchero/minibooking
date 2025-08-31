package com.minibooking.booking.adapters.in.http;

import com.minibooking.booking.adapters.out.mongo.BookingMongoRepository;
import com.minibooking.booking.application.service.BookingAppService;
import jakarta.validation.constraints.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingAppService app;
    private final BookingMongoRepository bookingQuery; // repo READ per il GET

    public BookingController(BookingAppService app, BookingMongoRepository bookingQuery) {
        this.app = app;
        this.bookingQuery = bookingQuery;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateBookingReq req) {
        var id = app.create(req.toCmd());
        return ResponseEntity.status(201).body(Map.of("bookingId", id.toString()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable UUID id) {
        return bookingQuery.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}

