package com.sparta.week4.service;

import com.sparta.week4.common.ChatClientProvider;
import com.sparta.week4.converter.ChatMessageConverter;
import com.sparta.week4.dto.ChatCompletionRequest;
import com.sparta.week4.dto.ChatCompletionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MyAiChatService implements AiChatService {

    private final Map<String, ChatClient> chatClients;

    private final ChatMessageConverter chatMessageConverter;

    private final ChatClientProvider chatClientProvider;

    @Override
    public ChatCompletionResponse chatSync(
            ChatCompletionRequest chatRequest
    ) {
        ChatClient chatClient = chatClientProvider.getChatClient(chatRequest.getModel());

        String promptMessage = chatMessageConverter.convertToPrompt(chatRequest.getMessages());

        ChatResponse chatResponse = chatClient.prompt()
                .user(u ->
                        u.text(promptMessage)
                ).call()
                .chatResponse();

        return chatMessageConverter.convertToResponse(chatResponse);
    }

    @Override
    public ChatCompletionResponse chatSteam(
            ChatCompletionRequest chatRequest
    ) {
        ChatClient chatClient = chatClientProvider.getChatClient(chatRequest.getModel());

        return null;
    }
}
