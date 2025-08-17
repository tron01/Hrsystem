package com.Abhijith.auth_service.service;

import com.Abhijith.auth_service.dto.ApiResponse;
import com.Abhijith.auth_service.dto.AuthRequest;
import com.Abhijith.auth_service.dto.RegisterRequest;
import com.Abhijith.auth_service.dto.UserInfoResponse;
import com.Abhijith.auth_service.model.User;
import com.Abhijith.auth_service.repository.UserRepository;
import com.Abhijith.auth_service.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public ResponseEntity<?> login(AuthRequest request, HttpServletResponse response) {
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        String username = authentication.getName();
        
        String roles = authentication.getAuthorities().stream()
                               .map(GrantedAuthority::getAuthority)
                               .collect(Collectors.joining(", "));
        
        log.info("-----------------------------------------");
        log.info("Logged in user: {}  roles: {}", username,roles);
        log.info("-----------------------------------------");
        
        String token = jwtUtil.generateToken(username, roles);
        
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        
        //cookie maxAge from jwt expiration time
        int cookieMaxAge = (int) (jwtUtil.jwtExpirationInMs / 1000);
        cookie.setMaxAge(cookieMaxAge);
        
        cookie.setPath("/");
        // cookie.setSecure(true); // enable in production
        response.addCookie(cookie);
        
        return ResponseEntity.ok(new ApiResponse("Login successful"));
    }
    
    public ResponseEntity<?> register(RegisterRequest request) {
        
        List<String> allowedRoles = List.of("USER", "HR","ADMIN");
        String requestedRole = request.getRole().toUpperCase();
        
        if (!allowedRoles.contains(requestedRole)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                           .body(Map.of("error", "Invalid role"));
        }
        
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                           .body(Map.of("error", "Username already exists"));
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                           .body(Map.of("error", "Email already exists"));
        }
        
        User user = User.builder()
                            .username(request.getUsername())
                            .email(request.getEmail())
                            .password(passwordEncoder.encode(request.getPassword()))
                            .role(requestedRole)
                            .enabled(true)
                            .createdAt(LocalDateTime.now())
                            .build();
        
        userRepository.save(user);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                       .body(Map.of("message", "User registered successfully"));
    }
    
    public ResponseEntity<?> logout(HttpServletResponse response) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        String username = authentication.getName(); // from JWT (subject)
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roles = authorities.stream()
                               .map(GrantedAuthority::getAuthority)
                               .collect(Collectors.joining(", "));
        
        // logout activity
        log.info("-------------------------------------------");
        log.info("User : {} Roles : {} :->logged out successfully", username, roles);
        log.info("-------------------------------------------");
        
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // delete immediately
        response.addCookie(cookie);
        
        return ResponseEntity.ok(Map.of("message", "User logout successfully"));
    }

    public ResponseEntity<?> getCurrentUser() {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        
        String username = authentication.getName(); // from JWT (subject)
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roles = authorities.stream()
                               .map(GrantedAuthority::getAuthority)
                               .collect(Collectors.joining(", "));
        
       
        log.info("----------------------------------------------");
        log.info("Authenticated user: {}  roles: {}", username, roles);
        log.info("------------------------------------------");
        return ResponseEntity.ok(new UserInfoResponse(username, roles));
        
    }
    
}
