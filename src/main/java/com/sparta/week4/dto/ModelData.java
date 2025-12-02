package com.sparta.week4.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ModelData {

    String id;

    String object;

    Long created;

    @JsonProperty("owned_by")
    String ownedBy;

}
