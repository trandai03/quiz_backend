package org.do_an.quiz_java.repositories;

import org.do_an.quiz_java.model.UserEssayAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEssayAnswerRepository extends JpaRepository<UserEssayAnswer, Integer> {

    void deleteByQuestionId(Integer questionId);

}