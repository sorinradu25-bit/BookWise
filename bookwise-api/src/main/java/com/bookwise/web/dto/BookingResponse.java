package com.bookwise.web.dto;

public record BookingResponse(
        Long id,
        Long slotId,
        Long userId,
        String status
) {}