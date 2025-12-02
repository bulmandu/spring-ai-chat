package com.sparta.week4.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ChatClientModelType {

    OLLAMA("ollamaChatClient", "qwen3", "ollama"),
    ANTHROPIC("anthropicChatClient", "claude-2", "claude")
    ;

    private final String chatClient;
    private final String model;
    private final String ownedBy;

    ChatClientModelType(String chatClient, String model, String ownedBy) {
        this.chatClient = chatClient;
        this.model = model;
        this.ownedBy = ownedBy;
    }

    public static ChatClientModelType fromModel(String model) {
        return Arrays.stream(values())
                .filter(type -> type.getModel().equals(model))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown model: " + model));
    }
}
