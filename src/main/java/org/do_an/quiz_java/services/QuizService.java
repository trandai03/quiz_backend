package org.do_an.quiz_java.services;

import lombok.RequiredArgsConstructor;
import org.do_an.quiz_java.exceptions.DataNotFoundException;
import org.do_an.quiz_java.exceptions.TokenExpiredException;
import org.do_an.quiz_java.model.Quiz;
import org.do_an.quiz_java.model.User;
import org.do_an.quiz_java.repositories.QuizRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizService {
    private QuizRepository quizRepository;

    private QuestionService questionService;
    public Quiz save(Quiz quiz, User user) {
        quiz.setCreatedBy(user);
        return quizRepository.save(quiz);
    }

    public Page<Quiz> findAll(Pageable pageable) {
        return quizRepository.findAll(pageable);
    }

    public Optional<Quiz> find(Integer id) throws DataNotFoundException {
        Optional<Quiz> quiz = quizRepository.findById(id);

        if (quiz == null) {
            throw new DataNotFoundException("Quiz " + id + " not found");
        }

        return quiz;
    }

//    public Quiz update(Quiz newQuiz) throws TokenExpiredException, DataNotFoundException {
//        Optional<Quiz> currentQuiz = find(newQuiz.getId());
//
//        mergeQuizzes(currentQuiz, newQuiz);
//        return quizRepository.save(currentQuiz);
//    }
    private void mergeQuizzes(Quiz currentQuiz, Quiz newQuiz) {
        currentQuiz.setTitle(newQuiz.getTitle());
        currentQuiz.setDescription(newQuiz.getDescription());
    }
}
