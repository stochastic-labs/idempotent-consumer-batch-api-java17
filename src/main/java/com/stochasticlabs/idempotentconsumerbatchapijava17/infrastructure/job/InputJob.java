package com.stochasticlabs.idempotentconsumerbatchapijava17.infrastructure.job;

import com.stochasticlabs.idempotentconsumerbatchapijava17.domain.entity.Input;
import com.stochasticlabs.idempotentconsumerbatchapijava17.domain.valueobject.Status;
import com.stochasticlabs.idempotentconsumerbatchapijava17.infrastructure.db.postgres.entity.InputScheduleEntity;
import com.stochasticlabs.idempotentconsumerbatchapijava17.infrastructure.db.postgres.repository.SpringDataPostgresInputRepository;
import com.stochasticlabs.idempotentconsumerbatchapijava17.infrastructure.messaging.dto.StochasticInputEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Configuration
public class InputJob {

    private static final Logger log = LoggerFactory.getLogger(InputJob.class);

    private final SpringDataPostgresInputRepository repository;

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.messaging.rabbit-exchange:stochastic-exchange}")
    private String exchange;

    @Value("${app.messaging.rabbit-routing-key:stochastic-routing-key}")
    private String routingKey;

    public InputJob(SpringDataPostgresInputRepository repository, RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Bean
    public RepositoryItemReader<InputScheduleEntity> myCustomReader() {
        return new RepositoryItemReaderBuilder<InputScheduleEntity>()
                .name("inputScheduleReader")
                .repository(repository)
                .methodName("findByStatusOrderByIdAsc")
                .arguments(List.of(Status.PENDING))
                .pageSize(10)
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemProcessor<InputScheduleEntity, InputScheduleEntity> myCustomProcessor() {
        return item -> {
            log.info("[my-batch-config-my-custom-processor] Process INTEGER [{}]", item.getInteger());
            item.setStatus(Status.PROCESSING);
            item.setUpdatedAt(LocalDateTime.now());
            return item;
        };
    }

    @Bean
    public ItemWriter<InputScheduleEntity> myCustomWriter() {
        return chunk -> {
            log.info("[my-batch-config-my-custom-writer] chunk.size [{}]", chunk.size());
            for (InputScheduleEntity entity : chunk) {

                Input input = new Input(entity.getInteger(), entity.getStatus());
                int stochasticValue = input.generateStochasticValue();

                StochasticInputEvent eventPayload = new StochasticInputEvent(input.integer(), stochasticValue);

                try {
                    rabbitTemplate.convertAndSend(exchange, routingKey, eventPayload);
                    entity.setStatus(Status.COMPLETED);
                    log.info("[my-batch-config-my-custom-writer] COMPLETED {} {}", input.integer(), stochasticValue);
                } catch (Throwable throwable) {
                    entity.setStatus(Status.FAILED);
                    entity.setErrorMessage(throwable.getMessage());
                    log.info("[my-batch-config-my-custom-writer] FAILED {} {} {}",
                            input.integer(), stochasticValue, throwable.getMessage());
                }
            }
            repository.saveAll(chunk);
        };
    }

    @Bean
    public Step myCustomStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("myCustomStep", jobRepository)
                .<InputScheduleEntity, InputScheduleEntity>chunk(10, transactionManager)
                .reader(myCustomReader())
                .processor(myCustomProcessor())
                .writer(myCustomWriter())
                .build();
    }

    @Bean
    public Job myCustomJob(JobRepository jobRepository, Step myCustomStep) {
        return new JobBuilder("myCustomJob", jobRepository)
                .start(myCustomStep)
                .build();
    }
}