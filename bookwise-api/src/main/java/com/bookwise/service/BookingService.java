package com.bookwise.service;

import com.bookwise.domain.AvailabilitySlot;
import com.bookwise.domain.Booking;
import com.bookwise.domain.BookingStatus;
import com.bookwise.domain.SlotStatus;
import com.bookwise.domain.User;
import com.bookwise.exception.ConflictException;
import com.bookwise.exception.NotFoundException;
import com.bookwise.repository.AvailabilitySlotRepository;
import com.bookwise.repository.BookingRepository;
import com.bookwise.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final AvailabilitySlotRepository slotRepository;
    private final UserRepository userRepository;

    public BookingService(
            BookingRepository bookingRepository,
            AvailabilitySlotRepository slotRepository,
            UserRepository userRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.slotRepository = slotRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Booking createBooking(Long slotId, String idempotencyKey) {
        User user = loadCurrentUser();

        // 1) Idempotency: return existing booking if same key was already used
        var existing = bookingRepository.findByUserIdAndIdempotencyKey(user.getId(), idempotencyKey);
        if (existing.isPresent()) {
            return existing.get();
        }

        try {
            // 2) Load slot
            AvailabilitySlot slot = slotRepository.findById(slotId)
                    .orElseThrow(() -> new NotFoundException("Slot not found: " + slotId));

            // 3) Check slot state
            if (slot.getStatus() != SlotStatus.AVAILABLE) {
                throw new ConflictException("Slot is not available");
            }

            // 4) Mark slot BOOKED (optimistic locking via @Version)
            slot.setStatus(SlotStatus.BOOKED);
            slotRepository.save(slot);

            // 5) Create booking (DB unique constraints protect double-booking + idempotency)
            Booking booking = new Booking(slot, user, BookingStatus.CREATED, idempotencyKey);
            return bookingRepository.save(booking);

        } catch (ObjectOptimisticLockingFailureException e) {
            throw new ConflictException("Slot was booked concurrently");
        } catch (DataIntegrityViolationException e) {
            // In race conditions, booking might exist now; return it if present.
            var nowExists = bookingRepository.findByUserIdAndIdempotencyKey(user.getId(), idempotencyKey);
            if (nowExists.isPresent()) {
                return nowExists.get();
            }
            throw new ConflictException("Booking conflict (slot already booked)");
        }
    }

    private User loadCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null || auth.getName().isBlank()) {
            throw new ConflictException("Unauthenticated");
        }

        String email = auth.getName(); // BasicAuth username
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found: " + email));
    }
}