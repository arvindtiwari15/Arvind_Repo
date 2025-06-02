package com.moviebooking.service;

import com.moviebooking.dto.theatre.TheatreRequest;
import com.moviebooking.dto.theatre.TheatreResponse;
import com.moviebooking.dto.show.ShowResponse;
import com.moviebooking.dto.theatre.TheatreWithShowsResponse;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.model.Theatre;
import com.moviebooking.model.User;
import com.moviebooking.model.Show;
import com.moviebooking.repository.TheatreRepository;
import com.moviebooking.repository.UserRepository;
import com.moviebooking.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TheatreService {

    private final TheatreRepository theatreRepository;
    private final UserRepository userRepository;
    private final ShowRepository showRepository;

    @Transactional
    public TheatreResponse createTheatre(TheatreRequest request, String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (theatreRepository.existsByNameAndCity(request.getName(), request.getCity())) {
            throw new RuntimeException("Theatre already exists in this city");
        }

        Theatre theatre = new Theatre();
        theatre.setName(request.getName());
        theatre.setAddress(request.getAddress());
        theatre.setCity(request.getCity());
        theatre.setState(request.getState());
        theatre.setCountry(request.getCountry());
        theatre.setOwner(owner);

        theatre = theatreRepository.save(theatre);
        return mapToResponse(theatre);
    }

    public List<TheatreResponse> getAllTheatres() {
        return theatreRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TheatreResponse> getTheatresByOwner(String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return theatreRepository.findByOwnerId(owner.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TheatreResponse> getTheatresByCity(String city) {
        return theatreRepository.findByCity(city).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TheatreResponse getTheatreById(UUID id) {
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found"));
        return mapToResponse(theatre);
    }

    @Transactional
    public TheatreResponse updateTheatre(UUID id, TheatreRequest request, String ownerEmail) {
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found"));

        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!theatre.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("Not authorized to update this theatre");
        }

        theatre.setName(request.getName());
        theatre.setAddress(request.getAddress());
        theatre.setCity(request.getCity());
        theatre.setState(request.getState());
        theatre.setCountry(request.getCountry());

        theatre = theatreRepository.save(theatre);
        return mapToResponse(theatre);
    }

    @Transactional
    public void deleteTheatre(UUID id, String ownerEmail) {
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found"));

        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!theatre.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("Not authorized to delete this theatre");
        }

        theatreRepository.delete(theatre);
    }

    public List<TheatreWithShowsResponse> getTheatresByMovieCityAndDate(UUID movieId, String city, LocalDateTime date) {
        List<Theatre> theatres = theatreRepository.findByCity(city);
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        
        List<TheatreWithShowsResponse> result = new java.util.ArrayList<>();
        for (Theatre theatre : theatres) {
            List<Show> shows = showRepository.findByTheatreIdAndMovieIdAndStartTimeBetween(
                theatre.getId(), movieId, startOfDay, endOfDay
            );
            if (!shows.isEmpty()) {
                TheatreWithShowsResponse resp = new TheatreWithShowsResponse();
                resp.setTheatre(mapToResponse(theatre));
                resp.setShows(shows.stream().map(show -> {
                    ShowResponse sr = new ShowResponse();
                    sr.setId(show.getId());
                    sr.setMovieId(show.getMovie().getId());
                    sr.setMovieTitle(show.getMovie().getTitle());
                    sr.setTheatreId(show.getTheatre().getId());
                    sr.setTheatreName(show.getTheatre().getName());
                    sr.setStartTime(show.getStartTime());
                    sr.setEndTime(show.getEndTime());
                    sr.setPrice(show.getPrice());
                    sr.setTotalSeats(show.getTotalSeats());
                    sr.setAvailableSeats(show.getAvailableSeats());
                    sr.setScreenNumber(show.getScreenNumber());
                    sr.setCreatedAt(show.getCreatedAt());
                    sr.setUpdatedAt(show.getUpdatedAt());
                    return sr;
                }).collect(java.util.stream.Collectors.toList()));
                result.add(resp);
            }
        }
        return result;
    }

    private TheatreResponse mapToResponse(Theatre theatre) {
        return TheatreResponse.builder()
                .id(theatre.getId())
                .name(theatre.getName())
                .address(theatre.getAddress())
                .city(theatre.getCity())
                .state(theatre.getState())
                .country(theatre.getCountry())
                .ownerId(theatre.getOwner().getId())
                .ownerName(theatre.getOwner().getFullName())
                .createdAt(theatre.getCreatedAt())
                .updatedAt(theatre.getUpdatedAt())
                .build();
    }
} 