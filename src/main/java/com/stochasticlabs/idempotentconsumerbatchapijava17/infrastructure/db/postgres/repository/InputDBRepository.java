package com.stochasticlabs.idempotentconsumerbatchapijava17.infrastructure.db.postgres.repository;

import com.stochasticlabs.idempotentconsumerbatchapijava17.domain.entity.Input;
import com.stochasticlabs.idempotentconsumerbatchapijava17.domain.repository.InputRepository;
import com.stochasticlabs.idempotentconsumerbatchapijava17.infrastructure.db.postgres.entity.InputScheduleEntity;
import com.stochasticlabs.idempotentconsumerbatchapijava17.interfaces.kafka.InputKafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class InputDBRepository implements InputRepository {

    private final SpringDataPostgresInputRepository springDataRepository;

    private static final Logger log = LoggerFactory.getLogger(InputKafkaConsumer.class);

    public InputDBRepository(SpringDataPostgresInputRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Input save(Input input) {
        InputScheduleEntity inputScheduleEntity = new InputScheduleEntity(input.integer(), input.status());

        InputScheduleEntity inputScheduleEntitySaved = springDataRepository.save(inputScheduleEntity);
        log.info("[input-db-repository-save] Input save success!: {}", inputScheduleEntitySaved.getInteger());
        return new Input(inputScheduleEntitySaved.getInteger(), inputScheduleEntitySaved.getStatus());
    }
}
