package com.notebook.portal.repository;

import com.notebook.portal.entity.Notebook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotebookRepository extends JpaRepository<Notebook, Long> {

    @Query("""
        select n from Notebook n
        where (:subject is null or lower(n.subject) like lower(concat('%', :subject, '%')))
          and (:semester is null or lower(n.semester) like lower(concat('%', :semester, '%')))
          and (:branch is null or lower(n.branch) like lower(concat('%', :branch, '%')))
          and (:keyword is null or lower(n.title) like lower(concat('%', :keyword, '%'))
               or lower(n.description) like lower(concat('%', :keyword, '%')))
    """)
    Page<Notebook> search(
        @Param("subject") String subject,
        @Param("semester") String semester,
        @Param("branch") String branch,
        @Param("keyword") String keyword,
        Pageable pageable
    );
}
