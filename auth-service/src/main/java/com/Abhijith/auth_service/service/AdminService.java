package com.Abhijith.auth_service.service;


import com.Abhijith.auth_service.dto.UserResponseDto;
import com.Abhijith.auth_service.model.User;
import com.Abhijith.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final UserRepository userRepository;
	
	public List<UserResponseDto> getAllUsers() {
		return userRepository.findAll()
				       .stream()
				       .map(this::toDto)
				       .toList();
	}
	public Optional<UserResponseDto> getUserById(String id) {
		return userRepository.findById(id)
				       .map(this::toDto);
	}
	
	public UserResponseDto updateUser(String id, User updatedUser) {
		return userRepository.findById(id)
				       .map(user -> {
					       user.setUsername(updatedUser.getUsername());
					       user.setEmail(updatedUser.getEmail());
					       user.setRole(updatedUser.getRole());
					       return toDto(userRepository.save(user));
				       })
				       .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
	}
	
	public UserResponseDto setUserEnabled(String id, boolean enabled) {
		return userRepository.findById(id)
				       .map(user -> {
					       user.setEnabled(enabled);
					       return toDto(userRepository.save(user));
				       })
				       .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
	}


	private UserResponseDto toDto(User user) {
		return UserResponseDto.builder()
				       .id(user.getId())
				       .username(user.getUsername())
				       .email(user.getEmail())
				       .role(user.getRole())
				       .createdAt(user.getCreatedAt())
				       .enabled(user.getEnabled())
				       .build();
	}
	
}
