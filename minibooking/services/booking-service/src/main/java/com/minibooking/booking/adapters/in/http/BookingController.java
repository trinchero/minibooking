package com.minibooking.booking.adapters.in.http;

import com.minibooking.booking.application.ports.BookingRepository;
import com.minibooking.booking.application.service.BookingAppService;
import com.minibooking.booking.domain.CreateBooking;
import jakarta.validation.constraints.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

record CreateBookingReq(@NotBlank String userId, @NotBlank String resourceType, String resourceRef,
                        @NotNull OffsetDateTime slotFrom, @NotNull OffsetDateTime slotTo,
                        Double amount, String currency) {
    CreateBooking toCmd(){ return new CreateBooking(userId,resourceType,resourceRef,slotFrom,slotTo,amount,currency); }
}

@RestController @RequestMapping("/bookings")
public class BookingController {
    private final BookingAppService app; private final BookingRepository repo;
    public BookingController(BookingAppService app, BookingRepository repo){ this.app=app; this.repo=repo; }

    @PostMapping public ResponseEntity<?> create(@RequestBody CreateBookingReq req){
        var id = app.create(req.toCmd());
        return ResponseEntity.status(201).body(Map.of("bookingId", id.toString()));
    }

/*
    @GetMapping("/{id}") public ResponseEntity<?> get(@PathVariable UUID id){
        return repo.find(id).<ResponseEntity<?>>map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
 */

    @GetMapping("/{id}") public ResponseEntity<?> get(@PathVariable UUID id){
        // Restituisci il documento Mongo (senza mappare al domain)
        var opt = ((com.minibooking.booking.adapters.out.mongo.BookingMongoRepo)
                org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor
                        .getBeanFactory(this).getBean(com.minibooking.booking.adapters.out.mongo.BookingMongoRepo.class))
                .findById(id);
        return opt.<ResponseEntity<?>>map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
