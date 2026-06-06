package com.stochasticlabs.idempotentconsumerbatchapijava17.interfaces.http.api.v1;

import com.stochasticlabs.idempotentconsumerbatchapijava17.application.dto.InputDTO;
import com.stochasticlabs.idempotentconsumerbatchapijava17.application.usecase.CreateInputUseCase;
import com.stochasticlabs.idempotentconsumerbatchapijava17.infrastructure.security.IdempotencyValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/idempotent")
@Tag(name = "Idempotent", description = "Endpoints to input payloads")
public class InputController {

    private final CreateInputUseCase createInputUseCase;

    private final IdempotencyValidator idempotencyValidator;

    public InputController(
            CreateInputUseCase createInputUseCase,
            IdempotencyValidator idempotencyValidator
    ) {
        this.createInputUseCase = createInputUseCase;
        this.idempotencyValidator = idempotencyValidator;
    }

    @Operation(summary = "Process input", description = "Process input")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payload processed success"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Failed all strategies")
    })
    @PostMapping("/input")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Validated @RequestBody InputDTO dto) {
        if (idempotencyValidator.isDuplicate(String.valueOf(dto))) {
            return;
        }
        createInputUseCase.create(dto);
    }
}
