package com.focalid.crudoperations.entity;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Person {

    @Id
    @GeneratedValue
    private int id;
    private String firstName;
    private String lastName;
    private String description;
    private int age;
    private String address;
    private String imagePath;
    private String imageName;
    private String imageType;
    private byte[] imageData;

    }


