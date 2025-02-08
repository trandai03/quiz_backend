package org.do_an.quiz_java.repositories;

import org.do_an.quiz_java.model.EssayQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EssayQuestionRepository extends JpaRepository<EssayQuestion, Integer> {
    void deleteById(Integer id);

    void deleteByQuizId(Integer id);

    List<EssayQuestion> findByQuizId(Integer id);
}