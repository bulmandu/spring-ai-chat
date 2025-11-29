package com.sparta.week4.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Choice {

    private Integer index;

    private MessageResponse message;

    private String finishReason;
}