package com.moviebooking.dto.show;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ShowRequest {
    @NotNull(message = "Movie ID is required")
    private UUID movieId;

    @NotNull(message = "Theatre ID is required")
    private UUID theatreId;

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private Double price;

    @NotNull(message = "Total seats is required")
    @Min(value = 1, message = "Total seats must be greater than 0")
    private Integer totalSeats;

    @NotNull(message = "Screen number is required")
    private String screenNumber;
} 