package com.Abhijith.job_posting_service.service;

import com.Abhijith.job_posting_service.dto.CreateJobDto;
import com.Abhijith.job_posting_service.dto.JobDto;
import com.Abhijith.job_posting_service.model.Job;
import com.Abhijith.job_posting_service.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;

    public List<JobDto> getAllJobsForUser() {
        return jobRepository.findAll().stream()
                       .filter(Job::isActive) // Filter only active jobs
                       .map(this::toDto)
                       .collect(Collectors.toList());
    }


    public Optional<JobDto> getJobById(String id) {
        return jobRepository.findById(id)
                       .filter(Job::isActive)
                       .map(this::toDto);
    }
    
  

// --------------------------------logged in HR  jobs methods--------------------------------------------------------//

    public List<JobDto> getAllJobsByCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        
        return jobRepository.findByPostedBy(username).stream()
                       .map(this::toDto)
                       .collect(Collectors.toList());
    }
    public Optional<JobDto> getJobByIdForCurrentUser(String id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        
        return jobRepository.findById(id)
                       .filter(job -> job.getPostedBy().equals(username))
                       .map(this::toDto);
    }

    public JobDto createJob(CreateJobDto dto) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Logged-in user ID
        
        Job job = Job.builder()
                          .title(dto.getTitle())
                          .description(dto.getDescription())
                          .company(dto.getCompany())
                          .location(dto.getLocation())
                          .salary(dto.getSalary())
                          .status(dto.getStatus())
                          .employmentType(dto.getEmploymentType())
                          .experienceLevel(dto.getExperienceLevel())
                          .skills(dto.getSkills())
                          .educationRequirement(dto.getEducationRequirement())
                          .industry(dto.getIndustry())
                          .closingDate(dto.getClosingDate())
                          .remote(dto.isRemote())
                          .postedDate(LocalDateTime.now())
                          .postedBy(username)
                          .active(true)
                          .build();
        
        return toDto(jobRepository.save(job));
    }

    public Optional<JobDto> updateJobByCurrentUser(String id, JobDto jobDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        
        return jobRepository.findById(id)
                       .filter(existing -> existing.getPostedBy().equals(username))
                       .map(existing -> {
                           // Only update allowed fields
                           BeanUtils.copyProperties(jobDto, existing, "id", "postedDate", "postedBy");
                           return toDto(jobRepository.save(existing));
                       });
    }

    public boolean deleteJobByCurrentUser(String id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        
        return jobRepository.findById(id)
                       .filter(existing -> existing.getPostedBy().equals(username))
                       .map(job -> {
                           jobRepository.deleteById(id);
                           return true;
                       })
                       .orElse(false);
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

