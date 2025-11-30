package com.sparta.week4;

import com.sparta.week4.dto.ChatCompletionRequest;
import com.sparta.week4.service.AiChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/chat")
@RequiredArgsConstructor
public class AiChatController {

    private final AiChatService aiChatService;

    @PostMapping("/completions")
    public ResponseEntity<?> completionsChat(
            @RequestBody @Valid ChatCompletionRequest chatRequest
    ) {
        if (chatRequest.getStream()) {
            Flux<ServerSentEvent<String>> fluxResponse = aiChatService.chatSteam(chatRequest);

            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .body(fluxResponse);
        }

        return ResponseEntity.ok(aiChatService.chatSync(chatRequest));
    }
}
