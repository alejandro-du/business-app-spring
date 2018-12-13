package com.example.issues.users;

import com.example.common.domain.Role;
import com.example.common.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository("issuesUserRepository")
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from Project p join p.members u where" + " p.id = :projectId" +
            " and lower(u.name) like concat('%', lower(:name), '%')" + " and (:role is null or u.role = :role)")
    Set<User> find(@Param("projectId") Long projectId, @Param("name") String name, @Param("role") Role role);

}
