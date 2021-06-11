package com.thefisola.job.management.system.mock;

import com.thefisola.job.management.system.constant.JobExecutionType;
import com.thefisola.job.management.system.constant.JobPriority;
import com.thefisola.job.management.system.constant.JobState;
import com.thefisola.job.management.system.dto.JobDto;
import com.thefisola.job.management.system.model.Job;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.UUID;

public class JobMock {

    public static Job getJob() {
        return Job.builder()
                .id(UUID.randomUUID().toString())
                .requestUrl("http://localhost:8091/sample-service/email")
                .requestPayload("{\\\"from\\\":\\\"randomemail@gmail.com\\\",\\\"to\\\":\\\"randomemail@yahoo.com\\\",\\\"body\\\":\\\"Random Email Body\\\",\\\"subject\\\":\\\"Random Email Subject!\\\"}\",\n" +
                        "\"tag\": \"send-email")
                .tag("send-email")
                .state(JobState.QUEUED)
                .priority(JobPriority.HIGH)
                .executionType(JobExecutionType.INSTANT)
                .dateExecuted(new Date())
                .dateCreated(new Date())
                .build();
    }

    public static JobDto getJobDto() {
        JobDto jobDto = new JobDto();
        BeanUtils.copyProperties(getJob(), jobDto);
        return jobDto;
    }
}
