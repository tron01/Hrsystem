package com.Abhijith.auth_service.service;

import com.Abhijith.auth_service.dto.AuthRequest;
import com.Abhijith.auth_service.dto.RegisterRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public ResponseEntity<Void> login(AuthRequest request, HttpServletResponse response) {
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        log.info("-----------------------------------------");
        log.info("Authenticated user: {}", authentication.getName());
        log.info("-----------------------------------------");
        String token = jwtUtil.generateToken(request.getUsername());
        
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        // cookie.setSecure(true); // enable in production
        response.addCookie(cookie);
        
        return ResponseEntity.ok().build();
    }
    
    public ResponseEntity<Void> register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // user already exists
        }
        
        User user = User.builder()
                            .username(request.getUsername())
                            .email(request.getEmail())
                            .password(passwordEncoder.encode(request.getPassword()))
                            .role("USER")
                            .enabled(true)
                            .createdAt(LocalDateTime.now())
                            .build();
        
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // delete immediately
        response.addCookie(cookie);
        
        return ResponseEntity.ok().build();
    }
}
