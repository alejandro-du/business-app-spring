package com.example.issues.issues;

import com.example.issues.projects.Project;
import com.example.api.domain.User;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Data
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Size(min = 3, max = 255)
    private String title;

    private String description;

    private LocalDate date;

    @ManyToOne
    private User owner;

    @ManyToOne
    @NotNull
    private User reporter;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;

    @ManyToOne
    @NotNull
    private Project project;

}
