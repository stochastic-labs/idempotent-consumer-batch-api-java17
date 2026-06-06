package com.stochasticlabs.idempotentconsumerbatchapijava17.infrastructure.db.postgres.repository;

import com.stochasticlabs.idempotentconsumerbatchapijava17.domain.valueobject.Status;
import com.stochasticlabs.idempotentconsumerbatchapijava17.infrastructure.db.postgres.entity.InputScheduleEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPostgresInputRepository extends JpaRepository<InputScheduleEntity, Long> {
    Slice<InputScheduleEntity> findByStatusOrderByIdAsc(Status status, PageRequest pageRequest);
}
