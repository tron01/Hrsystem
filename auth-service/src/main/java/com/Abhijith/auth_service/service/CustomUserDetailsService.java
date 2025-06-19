package com.Abhijith.auth_service.service;

import com.Abhijith.auth_service.model.User;
import com.Abhijith.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import static org.springframework.security.core.userdetails.User.*;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
		
		return withUsername(user.getUsername())
				       .password(user.getPassword())
				       .roles(user.getRole() != null ? user.getRole() : "USER")
				       .build();
	}
}