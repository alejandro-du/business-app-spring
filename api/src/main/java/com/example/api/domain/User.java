package com.example.api.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;

@Entity
@Data
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
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).*$", message = "must have a digit and a letter")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

}
