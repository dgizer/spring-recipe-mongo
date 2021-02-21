package com.springframework.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document
public class UnitOfMeasure {

    @Builder
    public UnitOfMeasure(String id, String description) {
        this.id = id;
        this.description = description;
    }

    @Id
    private String id;
    private String description;
}
