package com.stochasticlabs.idempotentconsumerbatchapijava17;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class IdempotentConsumerBatchApiJava17Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(IdempotentConsumerBatchApiJava17Application.class);

        String apiEnabled = System.getenv("APP_API_HTTP_ENABLED");

        if ("false".equalsIgnoreCase(apiEnabled)) {
            app.setWebApplicationType(WebApplicationType.NONE);
        }

        app.run(args);

    }
}
