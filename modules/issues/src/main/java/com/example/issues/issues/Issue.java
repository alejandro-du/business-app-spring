package com.example.issues.issues;

import com.example.common.domain.BusinessAppEntity;
import com.example.common.domain.User;
import com.example.issues.projects.Project;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Issue extends BusinessAppEntity {

    @NotNull
    @NotEmpty
    @Size(min = 3, max = 255)
    private String title;

    private String description;

    private LocalDate date;

    @ManyToOne
    @Where(clause = "deleted = false")
    private User owner;

    @ManyToOne
    @NotNull
    @Where(clause = "deleted = false")
    private User reporter;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;

    @ManyToOne
    @NotNull
    @Where(clause = "deleted = false")
    private Project project;

}
