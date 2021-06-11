package com.thefisola.job.management.system.service;

import com.thefisola.job.management.system.dto.JobDto;
import com.thefisola.job.management.system.model.Job;

import java.util.List;

public interface JobService {
    Job getJob(String jobId);

    Job createJob(JobDto jobDto);

    void executeJob(Job job);

    List<Job> getJobsReadyForExecution();
}
