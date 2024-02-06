package com.example.subscription.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import static jakarta.persistence.GenerationType.*;

@Entity
public class Member {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String email;
    private String name;
}
