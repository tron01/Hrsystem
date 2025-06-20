package com.Abhijith.job_posting_service.service;

import com.Abhijith.job_posting_service.dto.JobDto;
import com.Abhijith.job_posting_service.model.Job;
import com.Abhijith.job_posting_service.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminJobService {

	private final JobRepository jobRepository;
	
	public List<JobDto> getAllJobs() {
		return jobRepository.findAll().stream()
				       .map(this::toDto)
				       .collect(Collectors.toList());
	}
	
	public Optional<JobDto> getJobById(String id) {
		return jobRepository.findById(id)
				       .map(this::toDto);
	}


	public Optional<JobDto> updateJob(String id, JobDto jobDto) {
		return jobRepository.findById(id).map(existing -> {
			// Only copy updateable fields (skip id, postedDate, postedBy)
			BeanUtils.copyProperties(jobDto, existing,
					"id", "postedDate", "postedBy");
			return toDto(jobRepository.save(existing));
		});
	}
	
	public boolean deleteJob(String id) {
		if (jobRepository.existsById(id)) {
			jobRepository.deleteById(id);
			return true;
		}
		return false;
	}
	

//------------------------------ --- Mapping methods -----------------------------------------------------

	private JobDto toDto(Job job) {
		return JobDto.builder()
				       .id(job.getId())
				       .title(job.getTitle())
				       .description(job.getDescription())
				       .company(job.getCompany())
				       .location(job.getLocation())
				       .salary(job.getSalary())
				       .postedBy(job.getPostedBy())
				       .status(job.getStatus())
				       .employmentType(job.getEmploymentType())
				       .experienceLevel(job.getExperienceLevel())
				       .skills(job.getSkills())
				       .educationRequirement(job.getEducationRequirement())
				       .industry(job.getIndustry())
				       .postedDate(job.getPostedDate())
				       .closingDate(job.getClosingDate())
				       .remote(job.isRemote())
				       .active(job.isActive())
				       .build();
	}

}
