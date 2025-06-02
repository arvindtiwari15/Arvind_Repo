package com.moviebooking.dto.booking;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BookingResponse {
    private UUID id;
    private UUID userId;
    private String userName;
    private UUID showId;
    private String movieTitle;
    private String theatreName;
    private LocalDateTime showTime;
    private Integer numberOfSeats;
    private Double totalAmount;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 