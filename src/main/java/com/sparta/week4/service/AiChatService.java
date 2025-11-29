package com.sparta.week4.service;

import com.sparta.week4.dto.ChatCompletionRequest;
import com.sparta.week4.dto.ChatCompletionResponse;

public interface AiChatService {

    ChatCompletionResponse chatSync(ChatCompletionRequest chatRequest);

    ChatCompletionResponse chatSteam(ChatCompletionRequest chatRequest);
}
