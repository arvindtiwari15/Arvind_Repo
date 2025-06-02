package com.moviebooking.dto.show;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ShowResponse {
    private UUID id;
    private UUID movieId;
    private String movieTitle;
    private UUID theatreId;
    private String theatreName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double price;
    private Integer totalSeats;
    private Integer availableSeats;
    private String screenNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double pricePerSeat;
} 