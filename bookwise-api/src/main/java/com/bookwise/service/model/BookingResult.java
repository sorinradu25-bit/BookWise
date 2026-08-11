package com.bookwise.service.model;

import java.time.OffsetDateTime;

public record BookingResult(
        Long bookingId,
        Long slotId,
        Long customerId,
        String status,
        OffsetDateTime createdAt
) {}