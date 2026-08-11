package com.bookwise.repository;

import com.bookwise.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByUserIdAndIdempotencyKey(Long userId, String idempotencyKey);
}