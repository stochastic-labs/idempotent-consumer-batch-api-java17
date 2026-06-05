package com.stochasticlabs.idempotentconsumerbatchapijava17.domain.entity;

public record Input (int integer) {
    public Input {
        if (integer < 0) {
            throw new IllegalArgumentException("The value cannot be negative");
        }
    }
}
