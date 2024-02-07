package com.example.subscription.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.*;

@NoArgsConstructor
@Entity
public class Member {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String email;
    private String name;

    @Builder
    private Member(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
