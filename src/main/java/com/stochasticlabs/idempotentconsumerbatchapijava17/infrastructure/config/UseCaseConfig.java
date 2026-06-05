package com.stochasticlabs.idempotentconsumerbatchapijava17.infrastructure.config;

import com.stochasticlabs.idempotentconsumerbatchapijava17.application.usecase.CreateInputUseCase;
import com.stochasticlabs.idempotentconsumerbatchapijava17.infrastructure.db.postgres.repository.InputDBRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateInputUseCase createInputUseCase(InputDBRepository inputRepository) {
        return new CreateInputUseCase(inputRepository);
    }
}
