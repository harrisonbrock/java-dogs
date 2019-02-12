package com.harriosnbrock.dogs.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String bread;
    private int weight;
    private boolean suitableApartment;

    public Dog() {
    }

    public Dog(String bread, int weight, boolean suitableApartment) {
        this.bread = bread;
        this.weight = weight;
        this.suitableApartment = suitableApartment;
    }
}
