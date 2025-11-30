package com.sparta.week4.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatCompletionRequest {

    private String model;

    private List<Message> messages;

    private Double temperature;

    private Integer maxTokens;

    private Boolean stream;
}