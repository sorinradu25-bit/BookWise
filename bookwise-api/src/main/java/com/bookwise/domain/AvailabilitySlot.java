package com.bookwise.domain;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "availability_slots",
       indexes = {
           @Index(name = "idx_slots_provider_start", columnList = "provider_id,start_at")
       })


       
public class AvailabilitySlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    @Column(name = "start_at", nullable = false)
    private OffsetDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private OffsetDateTime endAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SlotStatus status;

    @Version
    @Column(nullable = false)
    private Long version;

    protected AvailabilitySlot() {}

    public AvailabilitySlot(Provider provider, OffsetDateTime startAt, OffsetDateTime endAt, SlotStatus status) {
        this.provider = provider;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
    }

        public Long getId() { return id; }
    public Provider getProvider() { return provider; }
    public OffsetDateTime getStartAt() { return startAt; }
    public OffsetDateTime getEndAt() { return endAt; }
    public SlotStatus getStatus() { return status; }

    public void setStatus(SlotStatus status) {
        this.status = status;
    }
}