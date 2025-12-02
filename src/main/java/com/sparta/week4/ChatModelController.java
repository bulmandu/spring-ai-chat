package com.sparta.week4;

import com.sparta.week4.dto.ChatModelResponse;
import com.sparta.week4.service.ChatModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/models")
@RequiredArgsConstructor
public class ChatModelController {

    private final ChatModelService chatModelService;

    @GetMapping
    public ResponseEntity<ChatModelResponse> getChatModels() {
        return ResponseEntity.ok(chatModelService.getChatModels());
    }

}
