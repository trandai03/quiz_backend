package org.do_an.quiz_java.repositories;

import org.do_an.quiz_java.model.Questionresult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionresultRepository extends JpaRepository<Questionresult, Integer> {
}