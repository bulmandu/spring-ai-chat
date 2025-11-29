package com.sparta.week4;

import com.sparta.week4.dto.ChatCompletionRequest;
import com.sparta.week4.dto.ChatCompletionResponse;
import com.sparta.week4.service.AiChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/chat")
@RequiredArgsConstructor
public class AiChatController {

    private final AiChatService aiChatService;

    @PostMapping("/completions")
    public ResponseEntity<ChatCompletionResponse> completionsChat(
            @RequestBody ChatCompletionRequest chatRequest
    ) {
        return ResponseEntity.ok(aiChatService.chatSync(chatRequest));
    }
}
