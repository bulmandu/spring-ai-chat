package com.sparta.week4.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AnthropicConfig {

    @Bean
    public ChatClient anthropicChatClient(
            AnthropicChatModel chatModel
    ) {
        return ChatClient.builder(chatModel)
                .defaultSystem("""
                당신은 친절하고 도움이 되는 AI 어시스턴트입니다.
                사용자의 질문에 정확하고 이해하기 쉽게 답변해주세요.
                """
                ).build();
    }
}
