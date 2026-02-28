package com.bookwise.service;

import java.time.OffsetDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bookwise.repository.AvailabilitySlotRepository;
import com.bookwise.web.dto.SlotResponse;
import com.bookwise.web.mapper.SlotMapper;

@Service
public class SlotService {

    private final AvailabilitySlotRepository slotRepository;

    public SlotService(AvailabilitySlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    public Page<SlotResponse> listSlots(Long providerId, OffsetDateTime from, OffsetDateTime to, Pageable pageable) {
        return slotRepository
                .findByProvider_IdAndStartAtGreaterThanEqualAndEndAtLessThanEqual(providerId, from, to, pageable)
                .map(SlotMapper::toResponse);
    }
}