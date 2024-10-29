package org.do_an.quiz_java.services;

import lombok.RequiredArgsConstructor;
import org.do_an.quiz_java.model.CompetitionQuiz;
import org.do_an.quiz_java.repositories.CompetitionQuizRepository;
import org.do_an.quiz_java.repositories.CompetitionRepository;
import org.do_an.quiz_java.repositories.QuizRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompetitionQuizService {
    private final CompetitionQuizRepository competitionQuizRepository;
    private final CompetitionRepository competitionRepository;
    private final QuizRepository quizRepository;
    public  void save(Integer quizId, Integer competitionId) {
        CompetitionQuiz competitionQuiz = CompetitionQuiz.builder()
                .competition(competitionRepository.findById(competitionId).get())
                .quiz(quizRepository.findById(quizId).get())
                .build();
        competitionQuizRepository.save(competitionQuiz);
    }



}
