package com.stochasticlabs.idempotentconsumerbatchapijava17.infrastructure.db.postgres.entity;

import com.stochasticlabs.idempotentconsumerbatchapijava17.domain.valueobject.Status;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "input_schedule")
public class InputScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "integer_value", nullable = false)
    private int integer;

    @Column(nullable = false, length = 20)
    private Status status;

    @Column(name = "error_message", length = 500)
    private String errorMessage;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public InputScheduleEntity() {}

    public InputScheduleEntity(int integer, Status status) {
        this.integer = integer;
        this.status = status;
    }

    public int getInteger() {
        return integer;
    }

    public Status getStatus() {
        return status;
    }
}
