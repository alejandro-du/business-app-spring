package com.example.api.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;

@Entity
@Where(clause = "deleted = false")
@Getter
@Setter
public class User extends BusinessAppEntity {

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    @NotEmpty
    @Size(min = 6, max = 255)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).*$", message = "{com.example.webapp.invalidPassword}")
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    private boolean deleted;

}
