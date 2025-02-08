package org.do_an.quiz_java.services;

import lombok.RequiredArgsConstructor;
import org.do_an.quiz_java.model.Competition;
import org.do_an.quiz_java.model.CompetitionQuiz;
import org.do_an.quiz_java.model.Quiz;
import org.do_an.quiz_java.repositories.CompetitionQuizRepository;
import org.do_an.quiz_java.repositories.CompetitionRepository;
import org.do_an.quiz_java.repositories.QuizRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompetitionQuizService {
    private final CompetitionQuizRepository competitionQuizRepository;
    private final CompetitionRepository competitionRepository;
    private final QuizRepository quizRepository;
    private final QuizService quizService;
    public  void save(Quiz quiz, Integer competitionId) {
        CompetitionQuiz competitionQuiz = CompetitionQuiz.builder()
                .competition(competitionRepository.findById(competitionId).get())
                .quiz(quiz)
                .build();
        competitionQuizRepository.save(competitionQuiz);
    }

    public void deleteQuizByCompetition(Competition competition) {

        List<CompetitionQuiz> competitionQuizzes = competitionQuizRepository.findByCompetition(competition);
        for (CompetitionQuiz competitionQuiz : competitionQuizzes) {
            quizService.deleteQuizByCompetitionQuiz(competitionQuiz);
//            quizRepository.deleteById(competitionQuiz.getQuiz().getId());
        }

    }

    public void deleteQuizByCompetitionAndQuiz(Competition competition, Quiz quiz) {
        CompetitionQuiz competitionQuiz = competitionQuizRepository.findByCompetitionAndQuiz(competition, quiz);
        quizRepository.deleteById(competitionQuiz.getQuiz().getId());
    }



}
