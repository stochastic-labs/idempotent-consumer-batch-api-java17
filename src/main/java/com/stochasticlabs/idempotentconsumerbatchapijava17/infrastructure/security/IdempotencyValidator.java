package com.stochasticlabs.idempotentconsumerbatchapijava17.infrastructure.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.time.Duration;

@Component
public class IdempotencyValidator {

    private final StringRedisTemplate redisTemplate;

    private static final Logger log = LoggerFactory.getLogger(IdempotencyValidator.class);

    public IdempotencyValidator(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isDuplicate(String idempotencyKey) {
        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent("lock:input:" + idempotencyKey, "PROCESSING", Duration.ofSeconds(30));

        boolean isDuplicate = Boolean.FALSE.equals(success);
        if (isDuplicate) {
            log.warn("[idempotency-validator-is-duplicate] Is Duplicate! {}", idempotencyKey);
        }

        return isDuplicate;
    }
}
