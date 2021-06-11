package com.thefisola.job.management.system.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thefisola.job.management.system.annotation.JsonString;
import com.thefisola.job.management.system.constant.CommonConstants;
import com.thefisola.job.management.system.constant.JobExecutionType;
import com.thefisola.job.management.system.constant.JobPriority;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class JobDto {

    @NotBlank(message = "Please provide the request url")
    @Pattern(message = "Please provide a valid URL", regexp = CommonConstants.VALID_URL_PATTERN)
    private String requestUrl;

    @JsonString(message = "Please provide a valid JSON string")
    @NotBlank(message = "Please provide the request payload")
    private String requestPayload;

    @NotBlank(message = "Please provide a tag describing the job")
    private String tag;

    Map<String, String> requestHeaders = new HashMap<>();

    private JobPriority priority = JobPriority.HIGH;

    private JobExecutionType executionType = JobExecutionType.INSTANT;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonConstants.DATETIME_FORMAT, timezone = CommonConstants.TIME_ZONE)
    private Date scheduledExecutionDate = new Date();
}
