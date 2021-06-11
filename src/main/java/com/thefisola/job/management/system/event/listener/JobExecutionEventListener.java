package com.thefisola.job.management.system.event.listener;

import com.thefisola.job.management.system.event.JobExecutionEvent;
import com.thefisola.job.management.system.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobExecutionEventListener {

    private final JobService jobService;

    @Autowired
    public JobExecutionEventListener(JobService jobService) {
        this.jobService = jobService;
    }

    @Async
    @EventListener
    public void handle(JobExecutionEvent jobExecutionEvent) {
        log.info("Handle execution of job with Id: {}", jobExecutionEvent.getJob().getId());
        var job = jobExecutionEvent.getJob();
        jobService.executeJob(job);
    }
}
