package org.do_an.quiz_java.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.do_an.quiz_java.dto.QuizDTO;
import org.do_an.quiz_java.exceptions.DataNotFoundException;
import org.do_an.quiz_java.model.Question;
import org.do_an.quiz_java.model.Quiz;
import org.do_an.quiz_java.model.User;
import org.do_an.quiz_java.repositories.QuizRepository;
import org.do_an.quiz_java.respones.quiz.QuizResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class QuizService {
    private final ModelMapper modelMapper;

    private final QuizRepository quizRepository;
    private final QuestionService questionService;
    private final CategoryService categoryService;

    public QuizResponse save(QuizDTO quizDTO, User user) {
        Quiz quiz = Quiz.builder()
                .title(quizDTO.getTitle())
                .category(categoryService.find(quizDTO.getCategory_id()))
                .createdBy(user)
                .description(quizDTO.getDescription())
                .isPublished(quizDTO.getIsPublished())
                .createdBy(user).build();
//        quizRepository.save(quiz);
        questionService.save(quizDTO.getQuestions(),quiz);
        quiz = quizRepository.save(quiz);
        return QuizResponse.fromEntity(quiz);
    }

    public Page<Quiz> findAll(Pageable pageable) {
        return quizRepository.findAll(pageable);
    }

    public Quiz findByQuizId(Integer id) throws DataNotFoundException {

        return quizRepository.findByQuizId(id);
    }

    public Page<Quiz> findAllPublished(Pageable pageable) {
        return quizRepository.findByIsPublishedTrue(pageable);
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

    public Page<Quiz> search(String query, Pageable pageable) {
        return quizRepository.searchByName(query, pageable);
    }

    public Page<Quiz> findQuizzesByUser(User user, Pageable pageable) {
        return quizRepository.findByCreatedBy(user, pageable);
    }
}
