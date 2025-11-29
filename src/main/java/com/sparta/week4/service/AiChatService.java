package com.sparta.week4.service;

import com.sparta.week4.dto.ChatCompletionRequest;
import com.sparta.week4.dto.ChatCompletionResponse;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

public interface AiChatService {

    ChatCompletionResponse chatSync(ChatCompletionRequest chatRequest);

    Flux<ServerSentEvent<String>> chatSteam(ChatCompletionRequest chatRequest);
}
