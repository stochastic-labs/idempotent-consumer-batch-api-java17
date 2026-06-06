package com.stochasticlabs.idempotentconsumerbatchapijava17.interfaces.kafka.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaErrorHandlerConfig {

    private static final Logger log = LoggerFactory.getLogger(KafkaErrorHandlerConfig.class);

    @Bean
    public CommonErrorHandler errorHandler(KafkaTemplate<Object, Object> template) {

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(template,
                (record, ex) -> {
                    log.error("[DLQ] Send record part {} offset {} for DLQ. Reason: {}",
                            record.partition(), record.offset(), ex.getCause().getMessage());
                    return new org.apache.kafka.common.TopicPartition(record.topic() + ".DLQ", record.partition());
                });

        FixedBackOff fixedBackOff = new FixedBackOff(5000L, 10L);
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, fixedBackOff);

        errorHandler.addNotRetryableExceptions(com.fasterxml.jackson.core.JsonProcessingException.class);

        return errorHandler;
    }
}
