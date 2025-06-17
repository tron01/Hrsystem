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
public class JobService {
    private final JobRepository jobRepository;

    public List<JobDto> getAllJobs() {
        return jobRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<JobDto> getJobById(String id) {
        return jobRepository.findById(id).map(this::toDto);
    }

    public JobDto createJob(JobDto jobDto) {
        Job job = toEntity(jobDto);
        job.setId(null);
        return toDto(jobRepository.save(job));
    }

    public Optional<JobDto> updateJob(String id, JobDto jobDto) {
        return jobRepository.findById(id).map(existing -> {
            BeanUtils.copyProperties(jobDto, existing, "id");
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

    private JobDto toDto(Job job) {
        return JobDto.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .company(job.getCompany())
                .location(job.getLocation())
                .salary(job.getSalary())
                .build();
    }

    private Job toEntity(JobDto dto) {
        return Job.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .company(dto.getCompany())
                .location(dto.getLocation())
                .salary(dto.getSalary())
                .build();
    }
}

