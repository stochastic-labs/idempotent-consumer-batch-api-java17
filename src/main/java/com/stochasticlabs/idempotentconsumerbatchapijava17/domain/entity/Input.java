package com.stochasticlabs.idempotentconsumerbatchapijava17.domain.entity;

import com.stochasticlabs.idempotentconsumerbatchapijava17.domain.valueobject.Status;

public record Input (int integer, Status status) {
    public Input {
        if (integer < 0) {
            throw new IllegalArgumentException("The value cannot be negative");
        }
    }
}
