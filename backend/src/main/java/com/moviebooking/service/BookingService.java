package com.moviebooking.service;

import com.moviebooking.dto.booking.BookingRequest;
import com.moviebooking.dto.booking.BookingResponse;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.exception.BusinessException;
import com.moviebooking.model.Booking;
import com.moviebooking.model.Show;
import com.moviebooking.model.User;
import com.moviebooking.repository.BookingRepository;
import com.moviebooking.repository.ShowRepository;
import com.moviebooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ShowRepository showRepository;
    private final UserRepository userRepository;

    @Transactional
    public BookingResponse createBooking(BookingRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));

        if (show.getStartTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Cannot book tickets for a show that has already started");
        }

        if (show.getAvailableSeats() < request.getNumberOfSeats()) {
            throw new BusinessException("Not enough seats available");
        }

        // Update available seats
        show.setAvailableSeats(show.getAvailableSeats() - request.getNumberOfSeats());
        showRepository.save(show);

        // Create booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setNumberOfSeats(request.getNumberOfSeats());
        booking.setTotalAmount(show.getPrice() * request.getNumberOfSeats());
        booking.setStatus("CONFIRMED");

        booking = bookingRepository.save(booking);
        return mapToResponse(booking);
    }

    public List<BookingResponse> getUserBookings(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return bookingRepository.findByUserId(user.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BookingResponse getBookingById(UUID id, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new BusinessException("Not authorized to view this booking");
        }

        return mapToResponse(booking);
    }

    @Transactional
    public void cancelBooking(UUID id, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new BusinessException("Not authorized to cancel this booking");
        }

        if (booking.getShow().getStartTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Cannot cancel booking for a show that has already started");
        }

        if ("CANCELLED".equals(booking.getStatus())) {
            throw new BusinessException("Booking is already cancelled");
        }

        // Update available seats
        Show show = booking.getShow();
        show.setAvailableSeats(show.getAvailableSeats() + booking.getNumberOfSeats());
        showRepository.save(show);

        // Update booking status
        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);
    }

    private BookingResponse mapToResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setUserId(booking.getUser().getId());
        response.setUserName(booking.getUser().getFullName());
        response.setShowId(booking.getShow().getId());
        response.setMovieTitle(booking.getShow().getMovie().getTitle());
        response.setTheatreName(booking.getShow().getTheatre().getName());
        response.setShowTime(booking.getShow().getStartTime());
        response.setNumberOfSeats(booking.getNumberOfSeats());
        response.setTotalAmount(booking.getTotalAmount());
        response.setStatus(booking.getStatus());
        response.setCreatedAt(booking.getCreatedAt());
        response.setUpdatedAt(booking.getUpdatedAt());
        return response;
    }
} 