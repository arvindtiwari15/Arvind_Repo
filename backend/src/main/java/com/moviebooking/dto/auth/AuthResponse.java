package com.moviebooking.dto.auth;

import com.moviebooking.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private UUID id;
    private String email;
    private String fullName;
    private UserRole role;
    private String token;
} 