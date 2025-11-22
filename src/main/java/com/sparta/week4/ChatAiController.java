package com.sparta.week4;

import com.sparta.week4.dto.ChatCompletionRequest;
import com.sparta.week4.dto.ChatCompletionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/vi/chat")
@RequiredArgsConstructor
public class ChatAiController {

    @PostMapping("/completions")
    public ResponseEntity<ChatCompletionResponse> completionsChat(
            @RequestBody ChatCompletionRequest chatRequest
    ) {
        return ResponseEntity.ok(new ChatCompletionResponse());
    }
}
