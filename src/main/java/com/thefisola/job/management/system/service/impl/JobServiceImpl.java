package com.thefisola.job.management.system.service.impl;

import com.thefisola.job.management.system.constant.JobState;
import com.thefisola.job.management.system.dto.JobDto;
import com.thefisola.job.management.system.event.JobExecutionEvent;
import com.thefisola.job.management.system.exception.JobNotFoundException;
import com.thefisola.job.management.system.model.Job;
import com.thefisola.job.management.system.repository.JobRepository;
import com.thefisola.job.management.system.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class JobServiceImpl implements JobService {

    private final RestTemplate restTemplate;
    private final JobRepository jobRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public JobServiceImpl(RestTemplateBuilder builder, JobRepository jobRepository, ApplicationEventPublisher eventPublisher) {
        this.restTemplate = builder.build();
        this.jobRepository = jobRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Job getJob(String jobId) {
        Optional<Job> job = jobRepository.findById(jobId);
        if (job.isEmpty()) throw new JobNotFoundException("Job with Id: '" + jobId + "' does not exist");
        return job.get();
    }

    @Override
    @Transactional
    public Job createJob(JobDto jobDto) {
        var job = Job.builder().build();
        job.fromJobDto(jobDto);
        job = jobRepository.save(job);
        if (job.requiresInstantExecution()) triggerJobExecution(job);
        return job;
    }

    @Override
    public void executeJob(Job job) {
        try {
            setJobState(job, JobState.RUNNING);
            ResponseEntity<Void> response = makeHttpRequest(job);
            JobState finalJobState = response.getStatusCode().is2xxSuccessful() ? JobState.SUCCESS : JobState.FAILED;
            setJobState(job, finalJobState);
            log.info("Job successfully executed {}", job);
        } catch (Exception e) {
            log.error("Error occurred while executing job {}, Error: {}", job.getId(), e);
            setJobState(job, JobState.FAILED);
        }
    }

    @Override
    public List<Job> getJobsReadyForExecution() {
        return jobRepository.findAllScheduledJobsReadyForExecution();
    }

    private ResponseEntity<Void> makeHttpRequest(Job job) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> httpEntity = new HttpEntity<>(job.getRequestPayload(), httpHeaders);
        return restTemplate.exchange(job.getRequestUrl(), HttpMethod.POST, httpEntity, Void.class);
    }

    public void triggerJobExecution(Job job) {
        eventPublisher.publishEvent(new JobExecutionEvent(job));
    }

    private void setJobState(Job job, JobState jobState) {
        if (jobState == JobState.FAILED || jobState == JobState.SUCCESS) job.setDateExecuted(new Date());
        job.setState(jobState);
        jobRepository.save(job);
    }
}
