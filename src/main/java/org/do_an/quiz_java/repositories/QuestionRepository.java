package org.do_an.quiz_java.repositories;

import org.do_an.quiz_java.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
}