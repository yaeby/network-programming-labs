package com.yaeby.np_lab_2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String link;
    private Double price;
    private Double priceOld;
    private Integer year;
    private String mileage;
    private String gearbox;
    private String fuel;
    private String engine;
    private String power;
    private String color;
    private String traction;
    private String bodyType;
    private Integer seats;
    private String consumption;
}
