package com.example.issues.projects;

import com.example.common.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByMembersIn(User user);

}
