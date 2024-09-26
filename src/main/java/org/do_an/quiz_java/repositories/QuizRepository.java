package org.do_an.quiz_java.repositories;

import org.do_an.quiz_java.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Integer> {
}