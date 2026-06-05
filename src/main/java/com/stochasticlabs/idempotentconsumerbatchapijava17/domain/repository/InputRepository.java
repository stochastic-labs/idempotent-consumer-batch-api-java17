package com.stochasticlabs.idempotentconsumerbatchapijava17.domain.repository;

import com.stochasticlabs.idempotentconsumerbatchapijava17.domain.entity.Input;

public interface InputRepository {
    public Input save(Input input);
}
