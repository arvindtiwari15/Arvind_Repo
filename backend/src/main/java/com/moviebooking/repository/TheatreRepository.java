package com.moviebooking.repository;

import com.moviebooking.model.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, UUID> {
    List<Theatre> findByOwnerId(UUID ownerId);
    List<Theatre> findByCity(String city);
    boolean existsByNameAndCity(String name, String city);
} 