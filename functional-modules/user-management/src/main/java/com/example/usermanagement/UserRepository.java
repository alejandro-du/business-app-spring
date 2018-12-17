package com.example.usermanagement;

import com.example.common.domain.Role;
import com.example.common.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository("usermanagementUserRepository")
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where lower(u.name) like concat('%', lower(:name), '%') and (:role is null or u.role = :role)")
    Set<User> find(@Param("name") String name, @Param("role") Role role);

}
