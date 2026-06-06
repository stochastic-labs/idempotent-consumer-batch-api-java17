package com.stochasticlabs.idempotentconsumerbatchapijava17.domain.entity;

import com.stochasticlabs.idempotentconsumerbatchapijava17.domain.valueobject.Status;

public record Input (int integer, Status status) {
    public Input {
        if (integer < 0) {
            throw new IllegalArgumentException("The value cannot be negative");
        }
    }

    public int generateStochasticValue() {
        long seed = integer;
        for (int i = 0; i < 10; i++) {
            seed = (seed * 1103515245L + 12345L) & 0x7FFFFFFF;
        }
        return (int) seed;
    }
}
