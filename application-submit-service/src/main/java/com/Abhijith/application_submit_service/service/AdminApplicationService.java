package com.Abhijith.application_submit_service.service;

import com.Abhijith.application_submit_service.dto.ApplicationDto;
import com.Abhijith.application_submit_service.model.Application;
import com.Abhijith.application_submit_service.repository.ApplicationRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AdminApplicationService {
	
	private ApplicationRepository applicationRepository;

	public List<ApplicationDto> getAllApplications() {
		return applicationRepository.findAll().stream()
				       .map(this::toDto)
				       .collect(Collectors.toList());
	}
	
	public ApplicationDto getApplicationById(String id) {
		return applicationRepository.findById(id)
				       .map(this::toDto)
				       .orElse(null);
	}

	public long getTotalApplicationCount() {
		return applicationRepository.count();
	}


	private ApplicationDto toDto(Application application) {
		ApplicationDto dto = new ApplicationDto();
		BeanUtils.copyProperties(application, dto);
		return dto;
	}
	
}
