package com.stochasticlabs.idempotentconsumerbatchapijava17.interfaces.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stochasticlabs.idempotentconsumerbatchapijava17.application.dto.InputDTO;
import com.stochasticlabs.idempotentconsumerbatchapijava17.application.usecase.CreateInputUseCase;
import com.stochasticlabs.idempotentconsumerbatchapijava17.infrastructure.security.IdempotencyValidator;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "app.kafka.consumer.enabled", havingValue = "true", matchIfMissing = false)
public class InputKafkaConsumer {

    private final CreateInputUseCase createInputUseCase;

    private final IdempotencyValidator idempotencyValidator;

    private final ObjectMapper objectMapper;

    private static final Logger log = LoggerFactory.getLogger(InputKafkaConsumer.class);

    public InputKafkaConsumer(
            CreateInputUseCase createInputUseCase,
            IdempotencyValidator idempotencyValidator,
            ObjectMapper objectMapper
    ) {
        this.createInputUseCase = createInputUseCase;
        this.idempotencyValidator = idempotencyValidator;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "${app.kafka.input-topic}",
            groupId = "${spring.kafka.consumer.group-id:idempotent-consumer-group}"
    )
    public void consume(ConsumerRecord<String, String> record) throws Exception {
        String json = record.value();
        log.info("[input-kafka-consumer-start] Start: {}", json);

        InputDTO dto = objectMapper.readValue(json, InputDTO.class);
        if (idempotencyValidator.isDuplicate(String.valueOf(dto))) {
            return;
        }

        createInputUseCase.create(dto);

        log.info("[input-kafka-consumer-end] End: {}", json);
    }
}
