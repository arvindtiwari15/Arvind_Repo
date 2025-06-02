package com.moviebooking.controller;

import com.moviebooking.dto.show.ShowRequest;
import com.moviebooking.dto.show.ShowResponse;
import com.moviebooking.service.ShowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shows")
@RequiredArgsConstructor
public class ShowController {
    private final ShowService showService;
    private static final Logger log = LoggerFactory.getLogger(ShowController.class);

    @PostMapping
    @PreAuthorize("hasRole('THEATRE_OWNER')")
    public ResponseEntity<ShowResponse> createShow(@Valid @RequestBody ShowRequest request) {
        log.info("[ShowController] createShow called with request: {}", request);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("[ShowController] Username from SecurityContext: {}", username);
        ShowResponse response = showService.createShow(request, username);
        log.info("[ShowController] Show created successfully: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowResponse> getShow(@PathVariable UUID id) {
        return ResponseEntity.ok(showService.getShow(id));
    }

    @GetMapping
    public ResponseEntity<List<ShowResponse>> getAllShows() {
        return ResponseEntity.ok(showService.getAllShows());
    }

    @GetMapping("/theatre/{theatreId}")
    public ResponseEntity<List<ShowResponse>> getShowsByTheatre(@PathVariable UUID theatreId) {
        return ResponseEntity.ok(showService.getShowsByTheatre(theatreId));
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ShowResponse>> getShowsByMovie(@PathVariable UUID movieId) {
        return ResponseEntity.ok(showService.getShowsByMovie(movieId));
    }

    @GetMapping("/theatre/{theatreId}/date-range")
    public ResponseEntity<List<ShowResponse>> getShowsByTheatreAndDateRange(
            @PathVariable UUID theatreId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(showService.getShowsByTheatreAndDateRange(theatreId, start, end));
    }

    @GetMapping("/movie/{movieId}/date-range")
    public ResponseEntity<List<ShowResponse>> getShowsByMovieAndDateRange(
            @PathVariable UUID movieId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(showService.getShowsByMovieAndDateRange(movieId, start, end));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('THEATRE_OWNER')")
    public ResponseEntity<ShowResponse> updateShow(
            @PathVariable UUID id,
            @Valid @RequestBody ShowRequest request) {
        return ResponseEntity.ok(showService.updateShow(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('THEATRE_OWNER')")
    public ResponseEntity<Void> deleteShow(@PathVariable UUID id) {
        showService.deleteShow(id);
        return ResponseEntity.noContent().build();
    }
} 