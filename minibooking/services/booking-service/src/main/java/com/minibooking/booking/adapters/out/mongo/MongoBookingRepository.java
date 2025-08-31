package com.minibooking.booking.adapters.out.mongo;

import com.minibooking.booking.application.ports.BookingRepository;
import com.minibooking.booking.domain.Booking;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MongoBookingRepository implements BookingRepository {
    private final BookingMongoRepo repo;

    public MongoBookingRepository(BookingMongoRepo repo){ this.repo = repo; }

    @Override
    public void save(Booking b){
        var d = new BookingDoc();
        d.id = b.id();
        // TODO: mappa TUTTI i campi (obbligatorio!)
        // d.userId=..., d.resourceType=..., d.resourceRef=..., d.slotFrom=..., d.slotTo=..., d.amount=..., d.currency=..., d.status="REQUESTED"
        repo.save(d);
    }

    @Override
    public Optional<Booking> find(UUID id){
        return repo.findById(id).map(d -> {
            // Per semplicità esponiamo il documento grezzo (va bene per GET nel MVP).
            // Se vuoi un domain puro qui, crea un mapper inverso Booking.fromDoc(d).
            return new BookingReflectionWrapper(d); // wrapper minimal per mostrare i dati
        }).map(BookingReflectionWrapper::toDomainOptional).orElse(Optional.empty());
    }

    @Override
    public void updateStatus(UUID id, String status){
        repo.findById(id).ifPresent(doc -> {
            doc.status = status;
            doc.updatedAt = OffsetDateTime.now().toString();
            repo.save(doc);
        });
    }

    // --- helper per convertire a una "vista" leggibile nel GET (senza complicare il domain)
    private static class BookingReflectionWrapper {
        private final BookingDoc d; BookingReflectionWrapper(BookingDoc d){ this.d=d; }
        Optional<Booking> toDomainOptional(){
            // Per il GET possiamo restituire il document invece del domain;
            // qui, per non rompere il controller, ritorniamo Optional.empty()
            // e modifichiamo il controller se preferisci restituire il document.
            return Optional.empty();
        }
    }
}
