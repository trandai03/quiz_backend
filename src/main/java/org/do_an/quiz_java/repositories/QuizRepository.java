package org.do_an.quiz_java.repositories;

import org.do_an.quiz_java.model.Quiz;
import org.do_an.quiz_java.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    Page<Quiz> findByIsPublishedTrue(Pageable pageable);

    Page<Quiz> findByCreatedBy(User user, Pageable pageable);

    @Query("select q from Quiz q where q.title like %?1%")
    Page<Quiz> searchByName(String name, Pageable pageable);

    @Query("select q from Quiz q where q.id = :id")
    Quiz findByQuizId(Integer id);
}