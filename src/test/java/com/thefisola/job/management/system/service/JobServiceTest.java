package com.thefisola.job.management.system.service;

import com.thefisola.job.management.system.base.BaseTest;
import com.thefisola.job.management.system.constant.JobState;
import com.thefisola.job.management.system.dto.JobDto;
import com.thefisola.job.management.system.event.JobExecutionEvent;
import com.thefisola.job.management.system.exception.JobNotFoundException;
import com.thefisola.job.management.system.mock.JobMock;
import com.thefisola.job.management.system.model.Job;
import com.thefisola.job.management.system.repository.JobRepository;
import com.thefisola.job.management.system.service.impl.JobServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Job service test")
class JobServiceTest extends BaseTest {

    @Autowired
    private JobService jobService;
    @MockBean
    private JobRepository jobRepository;
    @MockBean
    private ApplicationEventPublisher eventPublisher;
    @MockBean
    private RestTemplate restTemplate;
    @MockBean
    private RestTemplateBuilder builder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.when(builder.build())
                .thenReturn(restTemplate);
        jobService = new JobServiceImpl(builder, jobRepository, eventPublisher);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Test that method throws an exception when job does not exist")
    void test_that_throws_exception_when_job_does_not_exist() {
        Mockito.when(jobRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        String randomId = UUID.randomUUID().toString();
        assertThrows(JobNotFoundException.class, () -> {
            jobService.getJob(randomId);
        });
    }

    @Test
    @DisplayName("Test that job is gotten successfully when Id exist")
    void test_that_job_is_gotten_successfully() {
        String randomId = UUID.randomUUID().toString();
        Mockito.when(jobRepository.findById(randomId)).thenReturn(Optional.of(JobMock.getJob()));
        Job job = jobService.getJob(randomId);
        assertNotNull(job);
    }

    @Test
    @DisplayName("Test that job execution event is triggered for instant execution type")
    void test_that_job_execution_event_is_triggered_for_instant_job_type() {
        Mockito.when(jobRepository.save(Mockito.any())).thenReturn(JobMock.getJob());
        Mockito.doNothing().when(eventPublisher).publishEvent(Mockito.any(JobExecutionEvent.class));
        JobDto jobDto = JobMock.getJobDto();
        Job job = jobService.createJob(jobDto);
        Mockito.verify(eventPublisher, Mockito.times(1)).publishEvent(Mockito.any(JobExecutionEvent.class));
        assertNotNull(job);
    }

    private void mockHttpResponse(HttpStatus httpStatus) {
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<Void>>any()))
                .thenReturn(new ResponseEntity<>(httpStatus));
    }

    @Test
    @DisplayName("Test that job final state is 'success' if http status code is 200")
    void test_that_final_state_is_success_for_200_response() {
        mockHttpResponse(HttpStatus.OK);
        Job job = JobMock.getJob();
        jobService.executeJob(job);
        assertEquals(JobState.SUCCESS, job.getState());
    }

    @Test
    @DisplayName("Test that job final state is 'failed' if http status code is not 200")
    void test_that_final_state_is_failed_for_not_200_response() {
        mockHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR);
        Job job = JobMock.getJob();
        jobService.executeJob(job);
        assertEquals(JobState.FAILED, job.getState());
    }
}