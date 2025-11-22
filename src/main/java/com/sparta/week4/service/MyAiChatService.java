package com.sparta.week4.service;

import com.sparta.week4.converter.ChatResponseConverter;
import com.sparta.week4.dto.ChatCompletionRequest;
import com.sparta.week4.dto.ChatCompletionResponse;
import com.sparta.week4.dto.Message;
import com.sparta.week4.enums.ChatClientModelType;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.sparta.week4.enums.ChatClientModelType.fromModel;

@Service
@RequiredArgsConstructor
public class MyAiChatService implements AiChatService {

    private final Map<String, ChatClient> chatClients;

    private final ChatResponseConverter chatResponseConverter;

    @Override
    public ChatCompletionResponse chat(
            ChatCompletionRequest chatRequest
    ) {
        ChatClientModelType modelType = fromModel(chatRequest.getModel());
        ChatClient chatClient = chatClients.get(modelType.getChatClient());

        List<Message> messages = chatRequest.getMessages();
        String promptMessage = messages.stream()
                .filter(x -> x.getRole().equals("user"))
                .findFirst()
                .map(Message::getContent)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        ChatResponse chatResponse = chatClient.prompt()
                .user(u ->
                        u.text(promptMessage)
                ).call()
                .chatResponse();

        return chatResponseConverter.convert(chatResponse);
    }
}
