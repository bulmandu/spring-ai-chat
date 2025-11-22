package com.sparta.week4.config;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(OllamaProperties.class)
public class OllamaConfig {

    private final OllamaProperties ollamaProperties;

    @Bean
    public OllamaApi ollamaApi() {
        log.info("=== OllamaApi 생성 ===");

        return OllamaApi.builder()
                .baseUrl(ollamaProperties.baseUrl())
                .build();
    }

    @Bean(name = "ollamaChatModel")
    public OllamaChatModel ollamaChatModel(
            OllamaApi ollamaApi
    ) {
        log.info("=== OllamaChatModel 생성 ===");

        OllamaProperties.Chat.Options chatOptions = ollamaProperties.chat().options();
        OllamaOptions options = OllamaOptions.builder()
                .model(chatOptions.model())
                .temperature(chatOptions.temperature())
                .numPredict(chatOptions.numPredict())
                .topK(40)                         // Top-K 샘플링
                .topP(0.9)                        // Top-P (nucleus) 샘플링
                .repeatPenalty(1.1)               // 반복 방지
                .build();


        log.info("Ollama 모델: {}", options.getModel());
        log.info("Temperature: {}", options.getTemperature());

        return OllamaChatModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(options)
                .build();
    }

    @Bean
    public ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel) {
        return ChatClient.builder(ollamaChatModel)
                .defaultSystem("""
                당신은 친절하고 도움이 되는 AI 어시스턴트입니다.
                사용자의 질문에 정확하고 이해하기 쉽게 답변해주세요.
                """
                ).build();
    }
}
