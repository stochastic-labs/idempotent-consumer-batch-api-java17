package com.stochasticlabs.idempotentconsumerbatchapijava17.infrastructure.db;

import com.stochasticlabs.idempotentconsumerbatchapijava17.domain.entity.Input;
import com.stochasticlabs.idempotentconsumerbatchapijava17.domain.repository.InputRepository;

public class InputDBRepository implements InputRepository {
    public Input save(Input input) {
        return input;
    }
}
