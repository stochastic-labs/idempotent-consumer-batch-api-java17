package com.stochasticlabs.idempotentconsumerbatchapijava17.application.usecase;

import com.stochasticlabs.idempotentconsumerbatchapijava17.application.dto.InputDTO;
import com.stochasticlabs.idempotentconsumerbatchapijava17.domain.entity.Input;
import com.stochasticlabs.idempotentconsumerbatchapijava17.domain.valueobject.Status;
import com.stochasticlabs.idempotentconsumerbatchapijava17.domain.repository.InputRepository;

public class CreateInputUseCase {

    private InputRepository inputRepository;

    public CreateInputUseCase(InputRepository inputRepository) {
        this.inputRepository = inputRepository;
    }

    public void create(InputDTO inputDTO) {
        Input input = new Input(inputDTO.integer(), Status.PENDING);
        // TODO: idempotent
        inputRepository.save(input);
    }
}
