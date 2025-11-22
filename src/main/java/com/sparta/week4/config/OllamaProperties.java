package com.sparta.week4.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.ai.ollama")
public record OllamaProperties(
        String baseUrl,
        Chat chat
) {
    public record Chat(
            Options options
    ) {
        public record Options(
                String model,
                Double temperature,
                Integer numPredict
        ) {}
    }
}
