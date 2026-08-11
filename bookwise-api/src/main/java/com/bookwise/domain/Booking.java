package com.bookwise.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", nullable = false)
    private AvailabilitySlot slot;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Column(name = "idempotency_key", nullable = false, length = 100)
    private String idempotencyKey;

    protected Booking() {}

    public Booking(AvailabilitySlot slot, User user, BookingStatus status, String idempotencyKey) {
        this.slot = slot;
        this.user = user;
        this.status = status;
        this.idempotencyKey = idempotencyKey;
    }

    public Long getId() { return id; }
    public AvailabilitySlot getSlot() { return slot; }
    public User getUser() { return user; }
    public BookingStatus getStatus() { return status; }
    public String getIdempotencyKey() { return idempotencyKey; }
}