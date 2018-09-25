package com.example.issues.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @ManyToOne
    private User owner;

    @Enumerated(EnumType.STRING)
    private Status status;

}
