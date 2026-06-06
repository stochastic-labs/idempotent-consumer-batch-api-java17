package com.stochasticlabs.idempotentconsumerbatchapijava17.infrastructure.messaging.dto;

public record StochasticInputEvent(
        int originalValue,
        int stochasticValueGenerated
) {}
