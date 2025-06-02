package com.moviebooking.dto.movie;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MovieRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    @NotBlank(message = "Language is required")
    private String language;

    @NotBlank(message = "Genre is required")
    private String genre;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    @Max(value = 480, message = "Duration must be less than 480 minutes")
    private Integer durationMinutes;

    @NotNull(message = "Release date is required")
    @Future(message = "Release date must be in the future")
    private LocalDateTime releaseDate;

    private String posterUrl;
    private String trailerUrl;

    @NotBlank(message = "Director is required")
    private String director;

    @Size(max = 1000, message = "Cast members must be less than 1000 characters")
    private String castMembers;

    @NotNull(message = "Rating is required")
    @DecimalMin(value = "0.0", message = "Rating must be at least 0.0")
    @DecimalMax(value = "10.0", message = "Rating must be at most 10.0")
    private Double rating;
} 