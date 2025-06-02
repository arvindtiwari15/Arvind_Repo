package com.moviebooking.dto.movie;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MovieResponse {
    private UUID id;
    private String title;
    private String description;
    private String language;
    private String genre;
    private Integer durationMinutes;
    private LocalDateTime releaseDate;
    private String posterUrl;
    private String trailerUrl;
    private String director;
    private String castMembers;
    private Double rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 