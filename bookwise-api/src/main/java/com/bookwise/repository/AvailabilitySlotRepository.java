package com.bookwise.repository;

import java.time.OffsetDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bookwise.domain.AvailabilitySlot;

public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {

    Page<AvailabilitySlot> findByProvider_IdAndStartAtGreaterThanEqualAndEndAtLessThanEqual(
            Long providerId,
            OffsetDateTime from,
            OffsetDateTime to,
            Pageable pageable
    );
}