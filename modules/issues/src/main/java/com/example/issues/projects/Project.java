package com.example.issues.projects;

import com.example.api.domain.BusinessAppEntity;
import com.example.api.domain.User;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Data
public class Project extends BusinessAppEntity {

    @NotNull
    @NotEmpty
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> members;

}
