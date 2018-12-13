package com.example.issues.projects;

import com.example.common.domain.BusinessAppEntity;
import com.example.common.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Where(clause = "deleted = false")
@Getter
@Setter
public class Project extends BusinessAppEntity {

    @NotNull
    @NotEmpty
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> members;

    private boolean deleted;

}
