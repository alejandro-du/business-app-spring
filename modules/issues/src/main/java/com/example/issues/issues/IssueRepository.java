package com.example.issues.issues;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Set;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Query(value = "select i from Issue i left join i.owner o where" +
            " lower(i.title) like concat('%', lower(:title), '%')" +
            " and (:owner is null or lower(o.name) like concat('%', lower(:owner), '%'))" +
            " and lower(i.reporter.name) like concat('%', lower(:reporter), '%')" +
            " and (:status is null or i.status = :status)" +
            " and (:date is null or i.date = :date)")
    Set<Issue> find(
            @Param("title") String title,
            @Param("owner") String ownerName,
            @Param("reporter") String reporterName,
            @Param("status") Status status,
            @Param("date") LocalDate date
    );

}
