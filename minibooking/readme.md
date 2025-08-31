# 🏷️ minibooking

**Event-driven booking system demo** built with **Spring Boot 3, Kafka (Redpanda), MongoDB** and the **Outbox pattern**.  
Designed with **Hexagonal Architecture** and **Domain-Driven principles**.
---

## ✨ Features

- 📦 **BookingService**
    - REST API for creating bookings
    - Stores bookings in MongoDB
    - Outbox pattern to publish `booking.requested.v1`
    - Consumes lifecycle events to update booking status

- ⚙️ **ResourceService**
    - Consumes booking requests
    - Validates resource availability
    - Publishes `resource.events.v1`

- 🔄 **OrchestratorService**
    - Kafka Streams
    - Joins booking requests + resource events
    - Decides final booking status
    - Publishes `booking.lifecycle.v1`

- 🗃️ **MongoDB**
    - Used for bookings and outbox events
- 
- 🚀 **Redpanda**
    - Kafka-compatible broker
    - Easy local dev with Docker

---

## 🏃 Quickstart

### 1. Start platform (Kafka + Mongo)
```bash
cd platform
docker compose up -d
```
### 2. Start application
```bash
./gradlew :services:booking-service:bootRun
./gradlew :services:resource-service:bootRun
./gradlew :services:orchestrator-service:bootRun
```
### 3. Create a booking (by call or uging local sript)

```bash
./local.sh createReservation/booking.json  
```
OR
```bash
curl -s -X POST http://localhost:8081/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "u-1",
    "resourceType": "ITEM",
    "resourceRef": "ps5-001",
    "slotFrom": "2025-09-01T18:00:00Z",
    "slotTo": "2025-09-01T19:00:00Z",
    "amount": 49.9,
    "currency": "EUR"
  }'
```
### 4. Check status
```bash
curl -s http://localhost:8081/bookings/<bookingId> | jq .
```

## 📂 Project structure (Generated with AI)
```text
minibooking/
├── libs/
│   └── common-domain/         # domain events (records: BookingRequested, ResourceEvent, BookingLifecycle)
│
├── services/
│   ├── booking-service/       # REST API + Mongo (outbox) + consumer booking.lifecycle.v1
│   ├── resource-service/      # consumer booking.requested.v1 + producer resource.events.v1
│   └── orchestrator-service/  # Kafka Streams topology (joins requests + events → lifecycle)
│
├── platform/
│   └── docker-compose.yml     # Redpanda (Kafka) + MongoDB replica set
│
└── local/
    ├── createReservation/     # example JSON payloads (e.g. booking.json)
    └── local.sh               # helper script (POST booking + poll status)
```

## 🔎 Event Flow (Generated with AI)

```mermaid
sequenceDiagram
    autonumber
    actor Client as Client (curl / UI)
    participant BookingAPI as BookingService (REST API)
    participant Mongo as MongoDB (bookings + outbox)
    participant Outbox as OutboxPublisher (scheduler)
    participant Kafka as Kafka / Redpanda
    participant Resource as ResourceService
    participant Orchestrator as OrchestratorService (Kafka Streams)
    participant BookingConsumer as BookingService (Lifecycle Consumer)

    %% --- Booking creation ---
    Client->>BookingAPI: POST /bookings (CreateBooking)
    BookingAPI->>Mongo: Save booking [status=REQUESTED]\n+ insert OutboxEvent(NEW)
    BookingAPI-->>Client: 201 Created {bookingId}

    %% --- Outbox pattern ---
    Outbox->>Mongo: Claim NEW OutboxEvent
    Outbox->>Kafka: Publish booking.requested.v1
    Outbox->>Mongo: Mark OutboxEvent as PUBLISHED

    %% --- Resource processing ---
    Kafka-->>Resource: Consume booking.requested.v1
    Resource->>Kafka: Produce resource.events.v1 (RESERVED | REJECTED)

    %% --- Orchestration ---
    Kafka-->>Orchestrator: Join booking.requested + resource.events
    Orchestrator->>Kafka: Publish booking.lifecycle.v1 (CONFIRMED | CANCELLED)

    %% --- Update lifecycle ---
    Kafka-->>BookingConsumer: Consume booking.lifecycle.v1
    BookingConsumer->>Mongo: Update booking.status

    %% --- Query booking ---
    Client->>BookingAPI: GET /bookings/{id}
    BookingAPI->>Mongo: findById
    BookingAPI-->>Client: 200 Booking {status=CONFIRMED | CANCELLED}
    
    Legend:
    - **BookingService (REST API)** → creates reservation and send outbox message  
    - **OutboxPublisher** → send Kafka messages
    - **ResourceService** → decide which resource is free  
    - **OrchestratorService** → calculate the final state  
    - **BookingService (Consumer)** → update Mongo  
    - **Client** → makes POST/GET  

```