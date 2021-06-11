package com.thefisola.job.management.system.event;

import com.thefisola.job.management.system.model.Job;
import lombok.Getter;

@Getter
public class JobExecutionEvent {
    private final Job job;

    public JobExecutionEvent(Job job) {
        this.job = job;
    }
}
