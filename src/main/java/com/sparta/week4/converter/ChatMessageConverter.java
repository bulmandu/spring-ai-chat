package com.sparta.week4.converter;

import com.sparta.week4.dto.*;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class ChatMessageConverter {

    public String convertToPrompt(List<Message> messages) {
        return messages.stream()
                .filter(x -> x.getRole().equals("user"))
                .findFirst()
                .map(Message::getContent)
                .orElseThrow(() -> new RuntimeException("Message not found"));
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