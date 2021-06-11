package com.thefisola.job.management.system.controller;

import com.thefisola.job.management.system.dto.JobDto;
import com.thefisola.job.management.system.model.Job;
import com.thefisola.job.management.system.service.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/job")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<Job> getJob(@PathVariable String jobId) {
        return new ResponseEntity<>(jobService.getJob(jobId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Job> createJob(@Valid @RequestBody JobDto jobDto) {
        return new ResponseEntity<>(jobService.createJob(jobDto), HttpStatus.CREATED);
    }

}
