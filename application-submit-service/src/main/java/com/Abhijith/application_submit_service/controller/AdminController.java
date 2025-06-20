package com.Abhijith.application_submit_service.controller;

import com.Abhijith.application_submit_service.dto.ApplicationDto;
import com.Abhijith.application_submit_service.service.AdminApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin/applications")
@RequiredArgsConstructor
public class AdminController {

	private final AdminApplicationService adminService;
	
	@GetMapping("")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<ApplicationDto>> getAllApplications() {
		List<ApplicationDto> applications = adminService.getAllApplications();
		return ResponseEntity.ok(applications);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApplicationDto> getApplicationById(@PathVariable String id) {
		ApplicationDto applicationDto = adminService.getApplicationById(id);
		return applicationDto != null ? ResponseEntity.ok(applicationDto) : ResponseEntity.notFound().build();
	}

	@GetMapping("/count")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getTotalApplicationCount() {
		return ResponseEntity.ok(Map.of("count", adminService.getTotalApplicationCount()));
	}
	
}
