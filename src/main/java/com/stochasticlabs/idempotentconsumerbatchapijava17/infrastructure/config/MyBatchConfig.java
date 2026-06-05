package com.stochasticlabs.idempotentconsumerbatchapijava17.infrastructure.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class MyBatchConfig {

    @Bean
    public Step myCustomStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("myCustomStep", jobRepository)
                .<String, String>chunk(10, transactionManager)
                .reader(() -> null)
                .writer(chunk -> {})
                .build();
    }

    @Bean
    public Job myCustomJob(JobRepository jobRepository, Step myCustomStep) {
        return new JobBuilder("myCustomJob", jobRepository)
                .start(myCustomStep)
                .build();
    }
}