package com.bookwise.web.mapper;

import com.bookwise.domain.AvailabilitySlot;
import com.bookwise.web.dto.SlotResponse;

public class SlotMapper {

    private SlotMapper() {}

    public static SlotResponse toResponse(AvailabilitySlot slot) {
        return new SlotResponse(
                slot.getId(),
                slot.getProvider().getId(),
                slot.getStartAt(),
                slot.getEndAt(),
                slot.getStatus()
        );
    }
}