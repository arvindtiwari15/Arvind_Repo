package com.moviebooking.service;

import com.moviebooking.dto.movie.MovieRequest;
import com.moviebooking.dto.movie.MovieResponse;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.model.Movie;
import com.moviebooking.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    @Transactional
    public MovieResponse createMovie(MovieRequest request) {
        Movie movie = new Movie();
        updateMovieFromRequest(movie, request);
        return convertToResponse(movieRepository.save(movie));
    }

    @Transactional(readOnly = true)
    public MovieResponse getMovie(UUID id) {
        return convertToResponse(findMovieById(id));
    }

    @Transactional(readOnly = true)
    public List<MovieResponse> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovieResponse> searchMovies(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovieResponse> getMoviesByGenre(String genre) {
        return movieRepository.findByGenreIgnoreCase(genre).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovieResponse> getMoviesByLanguage(String language) {
        return movieRepository.findByLanguageIgnoreCase(language).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovieResponse> getUpcomingMovies() {
        return movieRepository.findByReleaseDateAfter(LocalDateTime.now()).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovieResponse> getTopRatedMovies(Double minRating) {
        return movieRepository.findByRatingGreaterThanEqual(minRating).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MovieResponse updateMovie(UUID id, MovieRequest request) {
        Movie movie = findMovieById(id);
        updateMovieFromRequest(movie, request);
        return convertToResponse(movieRepository.save(movie));
    }

    @Transactional
    public void deleteMovie(UUID id) {
        if (!movieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movie not found with id: " + id);
        }
        movieRepository.deleteById(id);
    }

    private Movie findMovieById(UUID id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
    }

    private void updateMovieFromRequest(Movie movie, MovieRequest request) {
        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setLanguage(request.getLanguage());
        movie.setGenre(request.getGenre());
        movie.setDurationMinutes(request.getDurationMinutes());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setPosterUrl(request.getPosterUrl());
        movie.setTrailerUrl(request.getTrailerUrl());
        movie.setDirector(request.getDirector());
        movie.setCastMembers(request.getCastMembers());
        movie.setRating(request.getRating());
    }

    private MovieResponse convertToResponse(Movie movie) {
        MovieResponse response = new MovieResponse();
        response.setId(movie.getId());
        response.setTitle(movie.getTitle());
        response.setDescription(movie.getDescription());
        response.setLanguage(movie.getLanguage());
        response.setGenre(movie.getGenre());
        response.setDurationMinutes(movie.getDurationMinutes());
        response.setReleaseDate(movie.getReleaseDate());
        response.setPosterUrl(movie.getPosterUrl());
        response.setTrailerUrl(movie.getTrailerUrl());
        response.setDirector(movie.getDirector());
        response.setCastMembers(movie.getCastMembers());
        response.setRating(movie.getRating());
        response.setCreatedAt(movie.getCreatedAt());
        response.setUpdatedAt(movie.getUpdatedAt());
        return response;
    }
} 