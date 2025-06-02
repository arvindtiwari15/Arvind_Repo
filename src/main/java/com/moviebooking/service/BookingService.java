package com.moviebooking.service;

import com.moviebooking.dto.BookingRequest;
import com.moviebooking.dto.BookingResponse;
import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request, Long userId);
    BookingResponse getBooking(Long bookingId);
    List<BookingResponse> getUserBookings(Long userId);
    BookingResponse cancelBooking(Long bookingId);
} 