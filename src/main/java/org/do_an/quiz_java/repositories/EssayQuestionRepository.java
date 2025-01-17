package org.do_an.quiz_java.repositories;

import org.do_an.quiz_java.model.EssayQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EssayQuestionRepository extends JpaRepository<EssayQuestion, Integer> {
}