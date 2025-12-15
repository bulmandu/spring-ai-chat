package com.sparta.week4.service;

import com.sparta.week4.common.ChatClientProvider;
import com.sparta.week4.converter.ChatMessageConverter;
import com.sparta.week4.dto.ChatCompletionRequest;
import com.sparta.week4.dto.ChatCompletionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyAiChatService implements AiChatService {

    private final ChatMessageConverter chatMessageConverter;

    private final ChatClientProvider chatClientProvider;

    @Override
    public ChatCompletionResponse chatSync(
            ChatCompletionRequest chatRequest
    ) {
        ChatClient chatClient = chatClientProvider.getChatClient(chatRequest.getModel());

        List<Message> messages = chatMessageConverter.convertToMessages(chatRequest.getMessages());

        ChatResponse chatResponse = chatClient.prompt()
                .messages(messages)
                .call()
                .chatResponse();

        return chatMessageConverter.convertToResponse(chatResponse);
    }

    @Override
    public Flux<ServerSentEvent<String>> chatSteam(
            ChatCompletionRequest chatRequest
    ) {
        ChatClient chatClient = chatClientProvider.getChatClient(chatRequest.getModel());

        List<Message> messages = chatMessageConverter.convertToMessages(chatRequest.getMessages());

        return chatClient.prompt()
                .messages(messages)
                .stream()
                .chatResponse()
                .map(mono -> ServerSentEvent.<String> builder()
                        .data(chatMessageConverter.convertToResponseJson(mono))
                        .build()
                ).concatWith(
                        Mono.just(
                                ServerSentEvent.<String> builder()
                                        .data("[DONE]")
                                        .build()
                        )
                );
    }
}
