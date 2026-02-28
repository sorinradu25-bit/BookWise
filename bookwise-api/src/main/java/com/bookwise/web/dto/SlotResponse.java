package com.bookwise.web.dto;

import java.time.OffsetDateTime;

import com.bookwise.domain.SlotStatus;

public record SlotResponse(
        Long id,
        Long providerId,
        OffsetDateTime startAt,
        OffsetDateTime endAt,
        SlotStatus status
) {}