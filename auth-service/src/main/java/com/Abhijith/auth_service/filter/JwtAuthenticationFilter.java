package com.Abhijith.auth_service.filter;

import com.Abhijith.auth_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String jwt = null;
        String username = null;
        
        // Try to get JWT from cookie
        if (request.getCookies() != null) {
            Cookie jwtCookie = Arrays.stream(request.getCookies())
                                       .filter(c -> c.getName().equals("jwt"))
                                       .findFirst().orElse(null);
            if (jwtCookie != null) {
                jwt = jwtCookie.getValue();
            }
        }
        
        // Fallback: Try to get JWT from Authorization header
        if (jwt == null) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwt = authHeader.substring(7);
            }
        }
        
        if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                
                username = jwtUtil.extractUsername(jwt);
               
                // Validate token before proceeding
                if (jwtUtil.isTokenValid(jwt, username)) {
                    String roles = jwtUtil.extractRoles(jwt);
                    List<GrantedAuthority> authorities = Arrays.stream(roles.split(","))
                                                                 .map(String::trim)
                                                                 .map(SimpleGrantedAuthority::new)
                                                                 .collect(Collectors.toList());
                    
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    log.warn("Invalid or expired JWT for user: {}", username);
                }
            } catch (Exception ex) {
                log.warn("Invalid JWT: {}", ex.getMessage());
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
