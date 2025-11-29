package com.sparta.week4.common;

import com.sparta.week4.enums.ChatClientModelType;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.sparta.week4.enums.ChatClientModelType.fromModel;

@Component
@RequiredArgsConstructor
public class ChatClientProvider {

    private final Map<String, ChatClient> chatClients;

    public ChatClient getChatClient(String modelName) {
        ChatClientModelType modelType = fromModel(modelName);
        ChatClient chatClient = chatClients.get(modelType.getChatClient());

        if (chatClient == null) {
            throw new IllegalArgumentException("ChatClient not found for model: " + modelName);
        }

        return chatClient;
    }
}
