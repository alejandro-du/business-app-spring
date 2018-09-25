package com.example.issues.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

    Set<User> findByNameContainingIgnoreCaseAndRole(String name, Role role);

    Set<User> findByNameContainingIgnoreCase(String name);

}
