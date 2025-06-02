package com.moviebooking.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Primary;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;
import org.springframework.security.core.Authentication;

@Slf4j
@Component
@Primary
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    public JwtTokenProvider() {
        log.info("[JwtTokenProvider] Constructor called");
        this.jwtSecret = Base64.getEncoder().encodeToString(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());
        this.jwtExpirationInMs = 86400000; // 24 hours
    }

    @PostConstruct
    public void init() {
        log.info("[JwtTokenProvider] Initializing with JWT secret: {}", jwtSecret);
        log.info("[JwtTokenProvider] JWT expiration: {} ms", jwtExpirationInMs);
        try {
            SecretKey key = getSecretKey();
            log.info("[JwtTokenProvider] Successfully generated secret key");
        } catch (Exception e) {
            log.error("[JwtTokenProvider] Failed to generate secret key", e);
        }
    }

    private SecretKey getSecretKey() {
        try {
            log.info("[JwtTokenProvider] Generating secret key from Base64 string: {}", jwtSecret);
            byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
            SecretKey key = Keys.hmacShaKeyFor(keyBytes);
            log.info("[JwtTokenProvider] Secret key generated successfully");
            return key;
        } catch (Exception e) {
            log.error("[JwtTokenProvider] Error creating secret key from Base64 string: {}", jwtSecret, e);
            throw new RuntimeException("Error creating secret key", e);
        }
    }

    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        try {
            return getClaimFromToken(token, Claims::getSubject);
        } catch (Exception e) {
            log.error("Error getting username from token", e);
            return null;
        }
    }

    public Date getExpirationDateFromToken(String token) {
        try {
            return getClaimFromToken(token, Claims::getExpiration);
        } catch (Exception e) {
            log.error("Error getting expiration date from token", e);
            return null;
        }
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        try {
            log.info("[JwtTokenProvider] Parsing claims from token");
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            log.info("[JwtTokenProvider] Successfully parsed claims from token");
            return claims;
        } catch (SignatureException e) {
            log.error("[JwtTokenProvider] Invalid JWT signature. Token: {}", token, e);
            throw e;
        } catch (MalformedJwtException e) {
            log.error("[JwtTokenProvider] Invalid JWT token format. Token: {}", token, e);
            throw e;
        } catch (ExpiredJwtException e) {
            log.error("[JwtTokenProvider] Expired JWT token. Token: {}", token, e);
            throw e;
        } catch (UnsupportedJwtException e) {
            log.error("[JwtTokenProvider] Unsupported JWT token. Token: {}", token, e);
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("[JwtTokenProvider] JWT claims string is empty. Token: {}", token, e);
            throw e;
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            log.info("[JwtTokenProvider] Validating token for user: {}", userDetails.getUsername());
            final String username = getUsernameFromToken(token);
            log.info("[JwtTokenProvider] Username from token: {}", username);
            final boolean isValid = username != null && 
                                  username.equals(userDetails.getUsername()) && 
                                  !isTokenExpired(token);
            log.info("[JwtTokenProvider] Token validation result for user {}: {}", username, isValid);
            return isValid;
        } catch (Exception e) {
            log.error("[JwtTokenProvider] Token validation failed for user: {}", userDetails.getUsername(), e);
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration != null && expiration.before(new Date());
    }
} 