package com.moviebooking.service;

import com.moviebooking.dto.show.ShowRequest;
import com.moviebooking.dto.show.ShowResponse;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.model.Movie;
import com.moviebooking.model.Show;
import com.moviebooking.model.Theatre;
import com.moviebooking.model.User;
import com.moviebooking.repository.MovieRepository;
import com.moviebooking.repository.ShowRepository;
import com.moviebooking.repository.TheatreRepository;
import com.moviebooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowService {
    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final TheatreRepository theatreRepository;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(ShowService.class);

    @Transactional
    public ShowResponse createShow(ShowRequest request, String ownerEmail) {
        log.info("[ShowService] createShow called with request: {} and ownerEmail: {}", request, ownerEmail);
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        log.info("[ShowService] Owner found: {}", owner);
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
        log.info("[ShowService] Movie found: {}", movie);
        Theatre theatre = theatreRepository.findById(request.getTheatreId())
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found"));
        log.info("[ShowService] Theatre found: {}", theatre);
        // Validate theatre ownership
        if (!theatre.getOwner().getId().equals(owner.getId())) {
            log.error("[ShowService] Theatre ownership validation failed. Theatre owner: {}, Current user: {}", theatre.getOwner().getId(), owner.getId());
            throw new RuntimeException("You are not authorized to create shows for this theatre");
        }
        log.info("[ShowService] Theatre ownership validated successfully");
        // Validate show timing
        validateShowTiming(request.getStartTime(), request.getEndTime(), movie.getDurationMinutes());
        log.info("[ShowService] Show timing validated successfully");
        Show show = new Show();
        show.setMovie(movie);
        show.setTheatre(theatre);
        show.setStartTime(request.getStartTime());
        show.setEndTime(request.getEndTime());
        show.setPrice(request.getPrice());
        show.setPricePerSeat(request.getPrice());
        show.setTotalSeats(request.getTotalSeats());
        show.setAvailableSeats(request.getTotalSeats());
        show.setScreenNumber(request.getScreenNumber());
        Show savedShow = showRepository.save(show);
        log.info("[ShowService] Show saved successfully: {}", savedShow);
        return convertToResponse(savedShow);
    }

    public ShowResponse getShow(UUID id) {
        return convertToResponse(findShowById(id));
    }

    public List<ShowResponse> getAllShows() {
        return showRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ShowResponse> getShowsByTheatre(UUID theatreId) {
        return showRepository.findByTheatreId(theatreId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ShowResponse> getShowsByMovie(UUID movieId) {
        return showRepository.findByMovieId(movieId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ShowResponse> getShowsByTheatreAndDateRange(UUID theatreId, LocalDateTime start, LocalDateTime end) {
        return showRepository.findByTheatreIdAndStartTimeBetween(theatreId, start, end).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ShowResponse> getShowsByMovieAndDateRange(UUID movieId, LocalDateTime start, LocalDateTime end) {
        return showRepository.findByMovieIdAndStartTimeBetween(movieId, start, end).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ShowResponse updateShow(UUID id, ShowRequest request) {
        Show show = findShowById(id);
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
        
        Theatre theatre = theatreRepository.findById(request.getTheatreId())
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found"));

        // Validate show timing
        validateShowTiming(request.getStartTime(), request.getEndTime(), movie.getDurationMinutes());

        show.setMovie(movie);
        show.setTheatre(theatre);
        show.setStartTime(request.getStartTime());
        show.setEndTime(request.getEndTime());
        show.setPrice(request.getPrice());
        show.setPricePerSeat(request.getPrice());
        show.setTotalSeats(request.getTotalSeats());
        show.setAvailableSeats(request.getTotalSeats());
        show.setScreenNumber(request.getScreenNumber());

        return convertToResponse(showRepository.save(show));
    }

    @Transactional
    public void deleteShow(UUID id) {
        Show show = findShowById(id);
        showRepository.delete(show);
    }

    private Show findShowById(UUID id) {
        return showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));
    }

    private ShowResponse convertToResponse(Show show) {
        ShowResponse response = new ShowResponse();
        response.setId(show.getId());
        response.setMovieId(show.getMovie().getId());
        response.setMovieTitle(show.getMovie().getTitle());
        response.setTheatreId(show.getTheatre().getId());
        response.setTheatreName(show.getTheatre().getName());
        response.setStartTime(show.getStartTime());
        response.setEndTime(show.getEndTime());
        response.setPrice(show.getPrice());
        response.setPricePerSeat(show.getPricePerSeat());
        response.setTotalSeats(show.getTotalSeats());
        response.setAvailableSeats(show.getAvailableSeats());
        response.setScreenNumber(show.getScreenNumber());
        response.setCreatedAt(show.getCreatedAt());
        response.setUpdatedAt(show.getUpdatedAt());
        return response;
    }

    private void validateShowTiming(LocalDateTime startTime, LocalDateTime endTime, int movieDuration) {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        long durationInMinutes = java.time.Duration.between(startTime, endTime).toMinutes();
        if (durationInMinutes < movieDuration) {
            throw new IllegalArgumentException("Show duration must be at least equal to movie duration");
        }
    }
} 