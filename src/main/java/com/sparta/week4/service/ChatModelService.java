package com.sparta.week4.service;

import com.sparta.week4.dto.ChatModelResponse;
import com.sparta.week4.dto.ModelData;
import com.sparta.week4.enums.ChatClientModelType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatModelService {

    public ChatModelResponse getChatModels() {
        log.info("Get Chat Models");
        List<ModelData> data = Arrays.stream(ChatClientModelType.values())
                .map(model -> new ModelData(
                            model.getModel(),
                            "model",
                            1686935002L,
                            model.getOwnedBy()
                        )
                ).toList();

        return new ChatModelResponse(
                "list",
                data
        );
    }
}
