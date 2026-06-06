package com.stochasticlabs.idempotentconsumerbatchapijava17.infrastructure.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InputJobScheduler {

    private final JobLauncher jobLauncher;

    private final Job myCustomJob;

    private static final Logger log = LoggerFactory.getLogger(InputJobScheduler.class);

    public InputJobScheduler(JobLauncher jobLauncher, Job myCustomJob) {
        this.jobLauncher = jobLauncher;
        this.myCustomJob = myCustomJob;
    }

    @Scheduled(fixedDelay = 10000)
    public void runPeriodicJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("runTime", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(myCustomJob, params);
        } catch (Exception e) {
            log.error("[input-job-scheduler-run-periodic-job] Error", e);
        }
    }
}
