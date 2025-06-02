package com.moviebooking.controller;

import com.moviebooking.dto.theatre.TheatreRequest;
import com.moviebooking.dto.theatre.TheatreResponse;
import com.moviebooking.dto.theatre.TheatreWithShowsResponse;
import com.moviebooking.service.TheatreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/theatres")
@RequiredArgsConstructor
public class TheatreController {

    private final TheatreService theatreService;
    private static final Logger logger = LoggerFactory.getLogger(TheatreController.class);

    @PostMapping
    @PreAuthorize("hasRole('THEATRE_OWNER')")
    public ResponseEntity<TheatreResponse> createTheatre(
            @Valid @RequestBody TheatreRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(theatreService.createTheatre(request, username));
    }

    @GetMapping
    public ResponseEntity<List<TheatreResponse>> getAllTheatres() {
        return ResponseEntity.ok(theatreService.getAllTheatres());
    }

    @GetMapping("/owner")
    @PreAuthorize("hasRole('THEATRE_OWNER')")
    public ResponseEntity<List<TheatreResponse>> getTheatresByOwner() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(theatreService.getTheatresByOwner(username));
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<TheatreResponse>> getTheatresByCity(@PathVariable String city) {
        return ResponseEntity.ok(theatreService.getTheatresByCity(city));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TheatreResponse> getTheatreById(@PathVariable UUID id) {
        return ResponseEntity.ok(theatreService.getTheatreById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('THEATRE_OWNER')")
    public ResponseEntity<TheatreResponse> updateTheatre(
            @PathVariable UUID id,
            @Valid @RequestBody TheatreRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(theatreService.updateTheatre(id, request, username));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('THEATRE_OWNER')")
    public ResponseEntity<Void> deleteTheatre(
            @PathVariable UUID id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        theatreService.deleteTheatre(id, username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/movie/{movieId}/city/{city}/date/{date}")
    public ResponseEntity<List<TheatreWithShowsResponse>> getTheatresByMovieCityAndDate(
            @PathVariable UUID movieId,
            @PathVariable String city,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime date) {
        logger.info("Received request for theatres by movie: {}, city: {}, date: {}", movieId, city, date);
        return ResponseEntity.ok(theatreService.getTheatresByMovieCityAndDate(movieId, city, date));
    }
} 