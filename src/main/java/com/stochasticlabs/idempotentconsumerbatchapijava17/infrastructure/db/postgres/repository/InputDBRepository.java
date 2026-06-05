package com.stochasticlabs.idempotentconsumerbatchapijava17.infrastructure.db.postgres.repository;

import com.stochasticlabs.idempotentconsumerbatchapijava17.domain.entity.Input;
import com.stochasticlabs.idempotentconsumerbatchapijava17.domain.repository.InputRepository;
import com.stochasticlabs.idempotentconsumerbatchapijava17.infrastructure.db.postgres.entity.InputScheduleEntity;
import org.springframework.stereotype.Repository;

@Repository
public class InputDBRepository implements InputRepository {

    private final SpringDataPostgresInputRepository springDataRepository;

    public InputDBRepository(SpringDataPostgresInputRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Input save(Input input) {
        InputScheduleEntity inputScheduleEntity = new InputScheduleEntity(input.integer(), input.status());

        InputScheduleEntity inputScheduleEntitySaved = springDataRepository.save(inputScheduleEntity);

        return new Input(inputScheduleEntitySaved.getInteger(), inputScheduleEntitySaved.getStatus());
    }
}
