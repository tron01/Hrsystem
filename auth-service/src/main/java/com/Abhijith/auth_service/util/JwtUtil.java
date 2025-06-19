package com.Abhijith.auth_service.util;


import com.Abhijith.auth_service.exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret:secret}")
    private String secretString;
    
    @Value("${jwt.expiration:3600000}")
    public long jwtExpirationInMs;
    
    private SecretKey key;
    
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretString.getBytes());
    }
    
    public String generateToken(String username, String roles) {
        return Jwts.builder()
                       .subject(username)
                       .claim("roles", roles)  // Add roles to payload
                       .issuedAt(new Date())
                       .expiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                       .signWith(key)
                       .compact();
    }
    
    
    public String extractRoles(String token) {
        return extractAllClaims(token).get("roles", String.class);
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                           .verifyWith(key)
                           .build()
                           .parseSignedClaims(token)
                           .getPayload();
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT for user {}: {}", e.getClaims().getSubject(), e.getMessage());
            throw new JwtAuthenticationException("Token expired");
        } catch (JwtException e) {
            log.warn("Invalid JWT: {}", e.getMessage());
            throw new JwtAuthenticationException("Token invalid" );
        }
    }


public boolean isTokenValid(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
    
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
