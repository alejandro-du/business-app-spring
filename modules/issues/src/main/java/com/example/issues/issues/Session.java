package com.example.issues.issues;

import com.example.api.domain.Role;
import com.example.issues.projects.Project;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
@Getter
@Setter
public class Session {

    private Long projectId;

    private Long userId;

    private Role role;

}
