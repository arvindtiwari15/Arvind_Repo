package com.moviebooking.service.impl;

import com.moviebooking.dto.BookingRequest;
import com.moviebooking.dto.BookingResponse;
import com.moviebooking.entity.Booking;
import com.moviebooking.entity.BookingStatus;
import com.moviebooking.entity.Show;
import com.moviebooking.entity.User;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.repository.BookingRepository;
import com.moviebooking.repository.ShowRepository;
import com.moviebooking.repository.UserRepository;
import com.moviebooking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ShowRepository showRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request, Long userId) {
        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Calculate total amount
        double totalAmount = show.getPrice() * request.getNumberOfSeats();

        Booking booking = new Booking();
        booking.setShow(show);
        booking.setUser(user);
        booking.setNumberOfSeats(request.getNumberOfSeats());
        booking.setTotalAmount(totalAmount);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus(BookingStatus.CONFIRMED);

        Booking savedBooking = bookingRepository.save(booking);
        return mapToResponse(savedBooking);
    }

    @Override
    public BookingResponse getBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        return mapToResponse(booking);
    }

    @Override
    public List<BookingResponse> getUserBookings(Long userId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId);
        return bookings.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingResponse cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        
        booking.setStatus(BookingStatus.CANCELLED);
        Booking updatedBooking = bookingRepository.save(booking);
        return mapToResponse(updatedBooking);
    }

    private BookingResponse mapToResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setShowId(booking.getShow().getId());
        response.setMovieName(booking.getShow().getMovie().getTitle());
        response.setTheaterName(booking.getShow().getTheater().getName());
        response.setNumberOfSeats(booking.getNumberOfSeats());
        response.setTotalAmount(booking.getTotalAmount());
        response.setBookingTime(booking.getBookingTime());
        response.setStatus(booking.getStatus().name());
        return response;
    }
} 