package com.example.issues.issues;

import com.example.api.domain.BusinessAppEntity;
import com.example.api.domain.User;
import com.example.issues.projects.Project;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Data
public class Issue extends BusinessAppEntity {

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
