package com.stochasticlabs.idempotentconsumerbatchapijava17.infrastructure.db.postgres.repository;

import com.stochasticlabs.idempotentconsumerbatchapijava17.infrastructure.db.postgres.entity.InputScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPostgresInputRepository extends JpaRepository<InputScheduleEntity, Long> {}
