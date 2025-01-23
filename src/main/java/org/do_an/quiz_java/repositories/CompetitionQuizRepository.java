package org.do_an.quiz_java.repositories;

import org.do_an.quiz_java.model.Competition;
import org.do_an.quiz_java.model.CompetitionQuiz;
import org.do_an.quiz_java.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CompetitionQuizRepository extends JpaRepository<CompetitionQuiz, Integer> {
    List<CompetitionQuiz> findByCompetition(Competition competition);

    CompetitionQuiz findByCompetitionAndQuiz(Competition competition, Quiz quiz);
}