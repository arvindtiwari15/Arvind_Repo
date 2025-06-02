package com.moviebooking.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookingResponse {
    private Long id;
    private Long showId;
    private String movieName;
    private String theaterName;
    private Integer numberOfSeats;
    private Double totalAmount;
    private LocalDateTime bookingTime;
    private String status;
} 