package com.harriosnbrock.dogs.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
//    @Column(name = "breed")
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
