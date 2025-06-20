package com.Abhijith.api_gateway.filter;

import com.Abhijith.api_gateway.exception.JwtAuthenticationException;
import com.Abhijith.api_gateway.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

private final JwtUtil jwtUtil;

@Override
protected void doFilterInternal(HttpServletRequest request,
                                HttpServletResponse response,
                                FilterChain filterChain)
		throws ServletException, IOException {
	
	Cookie[] cookies = request.getCookies();
	if (cookies != null) {
		for (Cookie cookie : cookies) {
			if ("jwt".equals(cookie.getName())) {
				String token = cookie.getValue();
				
				try {
					String username = jwtUtil.extractUsername(token);
					String roles = jwtUtil.extractRoles(token);
					
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							username,
							null,
							List.of(new SimpleGrantedAuthority(roles))  // assumes single role; adapt if list
					);
					
					SecurityContextHolder.getContext().setAuthentication(authentication);
					log.info("--------------------------------------------------------------");
					log.info("Authenticated user: {}, Roles: {}", username, roles);
					log.info("---------------------------------------------------------------");
					
				} catch (JwtAuthenticationException e) {
					log.warn("JWT validation failed: {}", e.getMessage());
				}
				
				break;
			}
		}
	}
	
	filterChain.doFilter(request, response);
}
}
