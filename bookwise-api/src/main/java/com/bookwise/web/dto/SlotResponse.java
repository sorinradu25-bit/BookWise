package com.bookwise.web.dto;

import com.bookwise.domain.SlotStatus;
import java.time.OffsetDateTime;

public record SlotResponse(
        Long id,
        Long providerId,
        OffsetDateTime startAt,
        OffsetDateTime endAt,
        SlotStatus status
) {}