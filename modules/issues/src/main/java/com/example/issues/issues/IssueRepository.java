package com.example.issues.issues;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Query("select i from Issue i join i.project p left join i.owner o where" +
            " p.id = :projectId" +
            " and lower(i.title) like concat('%', lower(:title), '%')" +
            " and (:owner is null or lower(o.name) like concat('%', lower(:owner), '%'))" +
            " and lower(i.reporter.name) like concat('%', lower(:reporter), '%')" +
            " and (:status is null or i.status = :status)" +
            " and (:date is null or i.date = :date)" +
            " and i.reporter.deleted = false" +
            " and (i.owner is null or i.owner.deleted = false)")
    Set<Issue> find(
            @Param("projectId") Long projectId,
            @Param("title") String title,
            @Param("owner") String ownerName,
            @Param("reporter") String reporterName,
            @Param("status") Status status,
            @Param("date") LocalDate date
    );

    @Query("select i from Issue i join i.project p join p.members m where" +
            " m.id = ?#{principal}" +
            " and i.reporter.deleted = false" +
            " and (i.owner is null or i.owner.deleted = false)" +
            " and i.id = :id" +
            " and i.reporter.deleted = false" +
            " and (i.owner is null or i.owner.deleted = false)")
    @Override
    Optional<Issue> findById(@Param("id") Long id);

}
