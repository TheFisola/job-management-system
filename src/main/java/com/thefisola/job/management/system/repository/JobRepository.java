package com.thefisola.job.management.system.repository;

import com.thefisola.job.management.system.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, String> {
    @Query("SELECT j, (CASE \n" +
            "WHEN (j.priority = 'HIGH') THEN 1 \n" +
            "WHEN (j.priority = 'MEDIUM') THEN 2 \n" +
            "WHEN (j.priority = 'LOW') THEN 3 END) as priority \n" +
            "FROM Job j WHERE j.state = 'QUEUED' AND j.executionType = 'SCHEDULED' \n" +
            "AND NOW() >= j.scheduledExecutionDate \n" +
            "ORDER BY priority")
    List<Job> findAllScheduledJobsReadyForExecution();
}
