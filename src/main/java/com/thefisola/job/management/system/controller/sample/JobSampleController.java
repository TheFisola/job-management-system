package com.thefisola.job.management.system.controller.sample;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class shows sample usage of the job management system
 */

@Slf4j
@RestController
@RequestMapping("/sample-service")
public class JobSampleController {

    /**
     * In this case {BASE_URL}/sample_service/email is the requestUrl
     * The json string is the requestPayload
     *
     * @param emailPayload
     * @return
     */
    @PostMapping("/email")
    public ResponseEntity<EmailPayload> sendEmail(@RequestBody EmailPayload emailPayload) {
        // Implementation of actual email sending job here
        log.info("Send Email Job Successful For Payload: {}", emailPayload);
        // A 200 response indicates Job was run successfully
        return new ResponseEntity<>(emailPayload, HttpStatus.OK);
    }

    @Getter
    @Setter
    @ToString
    static class EmailPayload {
        private String from;
        private String to;
        private String body;
        private String subject;
    }


}
