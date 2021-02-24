package com.springframework.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class Ingredient {

    //todo solve the problem with ID assignment via saving Ingredients in a separate Document in Mongo
    private String id = UUID.randomUUID().toString();
    private String description;
    private BigDecimal amount;
    private UnitOfMeasure uom;


    public Ingredient(String description, BigDecimal amount, UnitOfMeasure uom) {
        this.description = description;
        this.amount = amount;
        this.uom = uom;
    }

    public Ingredient() {
    }

}
