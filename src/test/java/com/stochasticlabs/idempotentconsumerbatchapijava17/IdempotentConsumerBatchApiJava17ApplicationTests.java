package com.stochasticlabs.idempotentconsumerbatchapijava17;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IdempotentConsumerBatchApiJava17ApplicationTests {

    @Test
    @DisplayName("Should start Spring Application!")
    void main_springApplication_start() {
        IdempotentConsumerBatchApiJava17Application.main(new String[] {"--server.port=0"});
    }
}
