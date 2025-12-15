package com.sparta.week4.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.week4.dto.*;
import com.sparta.week4.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChatMessageConverter {

    private final ObjectMapper objectMapper;

    public String convertToStringPrompt(List<ChatMessage> chatMessages) {
        return chatMessages.stream()
                .filter(x -> x.getRole().equals("user"))
                .findFirst()
                .map(ChatMessage::getContent)
                .orElseThrow(() -> new RuntimeException("ChatMessage not found"));
    }

    public List<Message> convertToMessages(
            List<ChatMessage> chatMessages
    ) {
        return chatMessages.stream()
                .map(msg -> {
                    return switch (msg.getRole()) {
                        case "user" -> new UserMessage(msg.getContent());
                        case "assistant" -> new AssistantMessage(msg.getContent());
                        default -> throw new RuntimeException("Unexpected role value: " + msg.getRole());
                    };
                })
                .collect(Collectors.toList());
    }

    public ChatCompletionResponse convertToResponse(
            ChatResponse chatResponse
    ) {
        ChatResponseMetadata responseMetadata = chatResponse.getMetadata();
        String id = responseMetadata.getId();
        String object = "chat.completion";
        Long created = Instant.now().getEpochSecond();
        String model = chatResponse.getMetadata().getModel();

        Usage usage = extractUsage(chatResponse);

        AtomicInteger index = new AtomicInteger();

        List<Choice> choices = chatResponse.getResults().stream()
                .map(result -> {
                    MessageResponse message = new MessageResponse(
                            result.getOutput().getMessageType().getValue(),
                            result.getOutput().getText()
                    );
                    return new Choice(
                            index.getAndIncrement(),
                            message,
                            result.getMetadata().getFinishReason() != null
                                    ? result.getMetadata().getFinishReason()
                                    : "stop"
                    );
                })
                .collect(Collectors.toList());

        return new ChatCompletionResponse(id, object, created, model, choices, usage);
    }

    public String convertToResponseJson(
            ChatResponse chatResponse
    ) {
        ChatCompletionResponse response = convertToResponse(chatResponse);
        try {
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert response to JSON", e);
        }
    }

    private Usage extractUsage(ChatResponse chatResponse) {
        ChatResponseMetadata metadata = chatResponse.getMetadata();

        if (metadata != null && metadata.getUsage() != null) {
            org.springframework.ai.chat.metadata.Usage aiUsage = metadata.getUsage();
            return new Usage(
                    aiUsage.getPromptTokens() != null ? aiUsage.getPromptTokens() : 0,
                    aiUsage.getCompletionTokens() != null ? aiUsage.getCompletionTokens() : 0,
                    aiUsage.getTotalTokens() != null ? aiUsage.getTotalTokens() : 0
            );
        }
        return new Usage(0, 0, 0);
    }
}