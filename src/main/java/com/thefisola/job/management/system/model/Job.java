package com.thefisola.job.management.system.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thefisola.job.management.system.constant.CommonConstants;
import com.thefisola.job.management.system.constant.JobExecutionType;
import com.thefisola.job.management.system.constant.JobPriority;
import com.thefisola.job.management.system.constant.JobState;
import com.thefisola.job.management.system.dto.JobDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "uuid2")
    private String id;

    private String requestUrl;

    private String requestPayload;

    private String tag;

    @ElementCollection
    @MapKeyColumn(name = "key")
    @Column(name = "value")
    @CollectionTable(name = "job_request_headers", joinColumns = @JoinColumn(name = "job_id"))
    Map<String, String> requestHeaders = new HashMap<>();

    @Enumerated(EnumType.STRING)
    private JobState state = JobState.QUEUED;

    @Enumerated(EnumType.STRING)
    private JobPriority priority;

    @Enumerated(EnumType.STRING)
    private JobExecutionType executionType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonConstants.DATETIME_FORMAT, timezone = CommonConstants.TIME_ZONE)
    private Date scheduledExecutionDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonConstants.DATETIME_FORMAT, timezone = CommonConstants.TIME_ZONE)
    private Date dateExecuted;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonConstants.DATETIME_FORMAT, timezone = CommonConstants.TIME_ZONE)
    private Date dateCreated;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonConstants.DATETIME_FORMAT, timezone = CommonConstants.TIME_ZONE)
    private Date dateUpdated;

    public void fromJobDto(JobDto jobDto) {
        BeanUtils.copyProperties(jobDto, this);
    }

    public boolean requiresInstantExecution() {
        return executionType == JobExecutionType.INSTANT;
    }
}
