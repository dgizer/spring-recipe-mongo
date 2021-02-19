package com.springframework.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UnitOfMeasure {

    @Builder
    public UnitOfMeasure(String id, String description) {
        this.id = id;
        this.description = description;
    }

    private String id;
    private String description;
}
