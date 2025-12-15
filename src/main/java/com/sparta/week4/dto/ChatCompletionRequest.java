package com.sparta.week4.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
public class ChatCompletionRequest {

    @NotNull
    private String model;

    @NotNull
    private List<ChatMessage> messages;

    private Double temperature;

    private Integer maxTokens;

    private Boolean stream = false;
}