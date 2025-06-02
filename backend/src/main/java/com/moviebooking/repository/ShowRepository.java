package com.moviebooking.repository;

import com.moviebooking.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ShowRepository extends JpaRepository<Show, UUID> {
    List<Show> findByTheatreId(UUID theatreId);
    List<Show> findByMovieId(UUID movieId);
    List<Show> findByTheatreIdAndStartTimeBetween(UUID theatreId, LocalDateTime start, LocalDateTime end);
    List<Show> findByMovieIdAndStartTimeBetween(UUID movieId, LocalDateTime start, LocalDateTime end);
    List<Show> findByTheatreIdAndMovieId(UUID theatreId, UUID movieId);
    List<Show> findByTheatreIdAndMovieIdAndStartTimeBetween(UUID theatreId, UUID movieId, LocalDateTime start, LocalDateTime end);
} 