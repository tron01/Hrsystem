package com.Abhijith.auth_service.controller;

import com.Abhijith.auth_service.dto.UserResponseDto;
import com.Abhijith.auth_service.model.User;
import com.Abhijith.auth_service.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;
	
	@GetMapping("/users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UserResponseDto>> getAllUsers() {
		return ResponseEntity.ok(adminService.getAllUsers());
	}
	
	@GetMapping("/users/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UserResponseDto> getUserById(@PathVariable String id) {
		return adminService.getUserById(id)
				       .map(ResponseEntity::ok)
				       .orElse(ResponseEntity.notFound().build());
	}
	
	@PutMapping("/users/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UserResponseDto> updateUser(@PathVariable String id, @RequestBody User updatedUser) {
		return ResponseEntity.ok(adminService.updateUser(id, updatedUser));
	}
	
	@PatchMapping("/users/{id}/enable")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UserResponseDto> setUserEnabled(@PathVariable String id, @RequestParam boolean enabled) {
		return ResponseEntity.ok(adminService.setUserEnabled(id, enabled));
	}
	
}
