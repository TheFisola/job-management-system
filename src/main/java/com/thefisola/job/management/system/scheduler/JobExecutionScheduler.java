package com.thefisola.job.management.system.scheduler;

import com.thefisola.job.management.system.event.JobExecutionEvent;
import com.thefisola.job.management.system.model.Job;
import com.thefisola.job.management.system.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class JobExecutionScheduler {

    private final JobService jobService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public JobExecutionScheduler(JobService jobService, ApplicationEventPublisher eventPublisher) {
        this.jobService = jobService;
        this.eventPublisher = eventPublisher;
    }

    @Scheduled(fixedDelayString = "${job.execution.scheduler.fixed.delay}")
    public void executeScheduledJobs() {
        List<Job> jobsReadyForExecution = jobService.getJobsReadyForExecution();
        if (!jobsReadyForExecution.isEmpty()) {
            log.info("Executing jobs scheduled for execution");
            jobsReadyForExecution.forEach(job -> eventPublisher.publishEvent(new JobExecutionEvent(job)));
        }
    }
}
