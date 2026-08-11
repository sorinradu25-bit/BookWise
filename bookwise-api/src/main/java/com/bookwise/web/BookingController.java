package com.bookwise.web;

import com.bookwise.domain.Booking;
import com.bookwise.service.BookingService;
import com.bookwise.web.dto.BookingResponse;
import com.bookwise.web.dto.CreateBookingRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse createBooking(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid @RequestBody CreateBookingRequest request
    ) {
        Booking booking = bookingService.createBooking(request.slotId(), idempotencyKey);

        return new BookingResponse(
                booking.getId(),
                booking.getSlot().getId(),
                booking.getUser().getId(),
                booking.getStatus().name()
        );
    }
}