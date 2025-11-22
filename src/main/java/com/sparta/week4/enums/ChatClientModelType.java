package com.sparta.week4.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ChatClientModelType {

    OLLAMA("ollamaChatClient", "qwen3"),
    ANTHROPIC("anthropicChatClient", "claude-2"),
    ;

    private final String chatClient;
    private final String model;

    ChatClientModelType(String chatClient, String model) {
        this.chatClient = chatClient;
        this.model = model;
    }

    public static ChatClientModelType fromModel(String model) {
        return Arrays.stream(values())
                .filter(type -> type.getModel().equals(model))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown model: " + model));
    }
}
